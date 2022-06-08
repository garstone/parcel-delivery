package kamenev.delivery.orderservice.dto;

import kamenev.delivery.orderservice.domain.Coordinates;
import kamenev.delivery.orderservice.domain.Status;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;


@Data
public class OrderDto {
    public UUID id;
    public UUID userId;
    public String userName;
    public String userPhone;
    public UUID courierId;
    public UUID courierName;
    public String courierPhone;
    public long orderNumber;
    public String destination;
    public Status status;
    public Coordinates coordinates;
    public LocalDateTime deliveryDatetime;
    public LocalDateTime pickupDatetime;
    public String comments;
    public Date createdAt;
    public Date updatedAt;
}
