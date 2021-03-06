package kamenev.delivery.orderservice.repository;

import kamenev.delivery.orderservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByUserId(UUID userId);

    List<Order> findAllByCourierId(UUID courierId);
}
