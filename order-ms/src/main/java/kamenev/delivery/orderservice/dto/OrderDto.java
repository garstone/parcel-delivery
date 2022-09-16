package kamenev.delivery.orderservice.dto;

import kamenev.delivery.orderservice.domain.Coordinates;
import kamenev.delivery.orderservice.domain.Status;

import java.time.LocalDateTime;
import java.util.UUID;


public record OrderDto (
        UUID id,
        UUID userId,
        String userName,
        String userPhone,
        String courierId,
        String courierName,
        String courierPhone,
        long orderNumber,
        String destination,
        Status status,
        Coordinates coordinates,
        LocalDateTime deliveryDatetime,
        LocalDateTime pickupDatetime,
        String comments,
        String createdAt,
        String updatedAt
        ) {
}
