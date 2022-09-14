package kamenev.delivery.orderservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Order {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_phone", nullable = false)
    private String userPhone;

    @Column(name = "courier_id")
    private UUID courierId;

    @Column(name = "courier_name")
    private String courierName;

    @Column(name = "courier_phone")
    private String courierPhone;

    @Column(name = "order_number")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderNumber;

    @Column
    private String destination;

    @Column(name = "pickup_location", nullable = false)
    private String pickupLocation;

    @Transient
    private Coordinates coordinates;

    @Enumerated(value = EnumType.STRING)
    @Column
    private Status status;

    @Column(name = "delivery_datetime")
    private LocalDateTime deliveryDatetime;

    @Column(name = "pickup_datetime")
    private LocalDateTime pickupDatetime;

    @Column
    private String comments;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

