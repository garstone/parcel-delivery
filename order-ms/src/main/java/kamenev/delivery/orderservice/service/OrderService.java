package kamenev.delivery.orderservice.service;

import kamenev.delivery.orderservice.domain.Coordinates;
import kamenev.delivery.orderservice.domain.Order;
import kamenev.delivery.orderservice.domain.Status;
import kamenev.delivery.orderservice.dto.OrderDetails;
import kamenev.delivery.orderservice.dto.OrderDto;
import kamenev.delivery.orderservice.mapper.OrderMapper;
import kamenev.delivery.orderservice.model.AssignToCourierRequest;
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
public class OrderService implements IOrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper = OrderMapper.INSTANCE;
//    private final KafkaProducer kafkaProducer;
    private final TrackingService trackingService;


    @Override
    public OrderDetails create(OrderCreateRequest request) {
        Order order = mapper.fromOrderCreateRequest(request);
        Order saved = repository.save(order);
        return mapper.toOrderDetailsResponse(saved);
    }

    @Override
    public OrderDetails changeDestination(UUID id, String destination) {
        Order order = getOrThrow(id);
        order.setDestination(destination);
        Order saved = repository.save(order);
        return mapper.toOrderDetailsResponse(saved);
    }

    @Override //todo нельзя отменить, если заказ уже у курьера
    public void cancel(UUID id) {
        var order = getOrThrow(id);
        order.setStatus(Status.CANCELED);
        order.setCoordinates(null);
        order.setCourierId(null);
        order.setCourierName(null);
        order.setCourierPhone(null);
        order.setDeliveryDatetime(null);
        order.setPickupDatetime(null);
        repository.save(order);
    }

    @Override
    public OrderDetails get(UUID id) {
        Order order = getOrThrow(id);
        return mapper.toOrderDetailsResponse(order);
    }

    @Override
    public List<OrderDetails> getByUserId(UUID userId) {
        List<Order> orders = repository.findAllByUserId(userId);
        if (orders.isEmpty()) {
            return List.of();
        }
        return orders.stream().map(mapper::toOrderDetailsResponse).toList();
    }

    @Override
    public List<OrderDetails> getByCourierId(UUID courierId) {
        List<Order> orders = repository.findAllByCourierId(courierId);
        return orders.stream().map(mapper::toOrderDetailsResponse).toList();
    }

    @Override
    public OrderDto changeStatus(UUID id, Status status) {
        Order order = getOrThrow(id);
        order.setStatus(status);
        order = repository.save(order);
        return mapper.toDto(order);
    }

    @Override
    public List<OrderDto> getAll() {
        List<Order> orders = repository.findAll();
        return orders.stream().map(mapper::toDto).toList();
    }

    @Override
    public OrderDto assign(AssignToCourierRequest request) {
        Order order = getOrThrow(request.orderId());
        order.setCourierId(request.courierId());
        order.setCourierName(request.name());
        order = repository.save(order);
        return mapper.toDto(order);
    }

    @Override
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
