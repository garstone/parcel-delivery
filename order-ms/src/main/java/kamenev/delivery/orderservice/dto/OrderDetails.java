package kamenev.delivery.orderservice.dto;

import kamenev.delivery.orderservice.domain.Coordinates;
import kamenev.delivery.orderservice.domain.Status;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class OrderDetails implements Serializable {
    private String userName;
    private String userPhone;
    private String courierName;
    private String courierPhone;
    private long orderNumber;
    private String destination;
    private Status status;
    private Coordinates coordinates;
    private LocalDateTime deliveryDatetime;
    private LocalDateTime pickupDatetime;
    private String comments;
    private Date createdAt;
    private Date updatedAt;
}
