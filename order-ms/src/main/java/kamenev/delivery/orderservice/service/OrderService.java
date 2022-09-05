package kamenev.delivery.orderservice.service;

import kamenev.delivery.orderservice.domain.Coordinates;
import kamenev.delivery.orderservice.domain.Order;
import kamenev.delivery.orderservice.domain.Status;
import kamenev.delivery.orderservice.dto.OrderDto;
import kamenev.delivery.orderservice.mapper.OrderMapper;
import kamenev.delivery.orderservice.messaging.KafkaProducer;
import kamenev.delivery.orderservice.model.AssignToCourierRequest;
import kamenev.delivery.orderservice.dto.OrderDetails;
import kamenev.delivery.orderservice.model.OrderCreateRequest;
import kamenev.delivery.orderservice.model.TrackResponse;
import kamenev.delivery.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper = OrderMapper.INSTANCE;
    private final KafkaProducer kafkaProducer;
    private final TrackingService trackingService;


    public OrderDetails create(OrderCreateRequest request) {
        Order order = mapper.fromOrderCreateRequest(request);
        Order saved = repository.save(order);
        return mapper.toOrderDetailsResponse(saved);
    }

    public OrderDetails changeDestination(UUID id, String destination) {
        Order order = getOrThrow(id);
        order.setDestination(destination);
        Order saved = repository.save(order);
        return mapper.toOrderDetailsResponse(saved);
    }

    public void cancel(UUID id) {
        changeStatus(id, Status.CANCELED);
    }

    public OrderDetails get(UUID id) {
        Order order = getOrThrow(id);
        return mapper.toOrderDetailsResponse(order);
    }

    public List<OrderDetails> getByUserId(UUID userId) {
        List<Order> orders = repository.findAllByUserId(userId);
        return orders.stream().map(mapper::toOrderDetailsResponse).toList();
    }

    public List<OrderDetails> getByCourierId(UUID courierId) {
        List<Order> orders = repository.findAllByCourierId(courierId);
        return orders.stream().map(mapper::toOrderDetailsResponse).toList();
    }

    public OrderDto changeStatus(UUID id, Status status) {
        Order order = getOrThrow(id);
        order.setStatus(status);
        order = repository.save(order);
        return mapper.toDto(order);
    }

    public List<OrderDto> getAll() {
        List<Order> orders = repository.findAll();
        return orders.stream().map(mapper::toDto).toList();
    }

    public OrderDto assign(AssignToCourierRequest request) {
        Order order = getOrThrow(request.orderId());
        order.setCourierId(request.courierId());
        order.setCourierName(request.name());
        order = repository.save(order);
        return mapper.toDto(order);
    }

    public TrackResponse getCoordinates(UUID orderId) {
        Order order = getOrThrow(orderId);
        Coordinates coordinates = trackingService.trackCourier(order.getCourierId());
        return new TrackResponse(coordinates.getLatitude(), coordinates.getLongitude());
    }

    private Order getOrThrow(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Order with id=%s does not exist", id)));
    }

}
