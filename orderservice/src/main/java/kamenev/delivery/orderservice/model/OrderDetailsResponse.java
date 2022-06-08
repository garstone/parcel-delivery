package kamenev.delivery.orderservice.model;

import kamenev.delivery.orderservice.domain.Coordinates;
import kamenev.delivery.orderservice.domain.Status;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
public class OrderDetailsResponse {
    public String userName;
    public String userPhone;
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
