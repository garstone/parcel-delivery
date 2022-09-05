package kamenev.delivery.orderservice.service;

import kamenev.delivery.orderservice.domain.Order;
import kamenev.delivery.orderservice.model.OrderCreateRequest;
import kamenev.delivery.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Autowired
    private ApplicationContext context;

    @Test
    void create_Ok() {
        var orderCreateReq = new OrderCreateRequest(UUID.randomUUID(), "+79669991212", "Vladimir",
                "Moscow, Kremlin", null);
        var captor = ArgumentCaptor.forClass(Order.class);
        var resultOrder = new Order();
        resultOrder.setOrderNumber(1);
        when(orderRepository.save(captor.capture())).thenReturn(resultOrder);

        var res = orderService.create(orderCreateReq);
        assertEquals(resultOrder.getOrderNumber(), res.getOrderNumber());
        assertEquals(orderCreateReq.userId(), captor.getValue().getUserId());
    }

    @Test
    void changeDestination() {
    }

    @Test
    void cancel() {
    }

    @Test
    void get() {
    }

    @Test
    void getByUserId() {
    }

    @Test
    void getByCourierId() {
    }

    @Test
    void changeStatus() {
    }

    @Test
    void getAll() {
    }

    @Test
    void assign() {
    }

    @Test
    void getCoordinates() {
    }
}