package kamenev.delivery.orderservice.service;

import kamenev.delivery.orderservice.domain.Status;
import kamenev.delivery.orderservice.dto.OrderDetails;
import kamenev.delivery.orderservice.dto.OrderDto;
import kamenev.delivery.orderservice.model.AssignToCourierRequest;
import kamenev.delivery.orderservice.model.OrderCreateRequest;
import kamenev.delivery.orderservice.model.TrackResponse;

import java.util.List;
import java.util.UUID;

public interface IOrderService {
    OrderDetails create(OrderCreateRequest request);

    OrderDetails changeDestination(UUID id, String destination);

    void cancel(UUID id);

    OrderDetails get(UUID id);

    List<OrderDetails> getByUserId(UUID userId);

    List<OrderDetails> getByCourierId(UUID courierId);

    OrderDto changeStatus(UUID id, Status status);

    List<OrderDto> getAll();

    OrderDto assign(AssignToCourierRequest request);

    TrackResponse getCoordinates(UUID orderId);
}
