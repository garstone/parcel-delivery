package kamenev.delivery.orderservice.service;

import kamenev.delivery.orderservice.domain.Status;
import kamenev.delivery.orderservice.integration.AbstractDbTest;
import kamenev.delivery.orderservice.model.AssignToCourierRequest;
import kamenev.delivery.orderservice.model.OrderCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderServiceTest extends AbstractDbTest {

    @Autowired
    private IOrderService orderService;
    private static final String[] EXCLUDED_COLS = {"id", "created_at", "updated_at"};

    @Test
    void create_Ok() throws Exception {
        var orderCreateReq = new OrderCreateRequest(p.userId1, "+79991234567", "Vladimir",
                "Moscow, Kremlin", "Moscow", "comments");
        var res = orderService.create(orderCreateReq);
        assertData("expected.create.ok.xml", EXCLUDED_COLS);
    }

    @Test
    void changeDestination_Ok() throws Exception {
        setupData("setup.base.orders.xml");
        var res = orderService.changeDestination(p.id1, "New destination");
        assertData("expected.change_destination.ok.xml", EXCLUDED_COLS);
    }

    @Test
    void changeDestination_OrderNotExists() throws Exception {
        setupData("setup.base.orders.xml");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                orderService.changeDestination(p.id3, "New destination"));
        assertEquals("Order with id="+p.id3+" does not exist",
                exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void cancel_Ok() throws Exception {
        setupData("setup.base.orders.xml");
        orderService.cancel(p.id1);
        assertData("expected.cancel_order.ok.xml", EXCLUDED_COLS);
    }

    @Test
    void cancel_OrderNotExist() throws Exception {
        setupData("setup.base.orders.xml");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                orderService.cancel(p.id3));
        assertEquals("Order with id="+p.id3+" does not exist",
                exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void get() throws Exception {
        setupData("setup.base.orders.xml");
        var res = orderService.get(p.id1);
        assertNotNull(res);
        assertEquals(0, res.getOrderNumber());
        assertEquals("Vladimir", res.getUserName());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                orderService.cancel(p.id3));
        assertEquals("Order with id="+p.id3+" does not exist",
                exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void getByUserId_NotFound() throws Exception {
        setupData("setup.base.orders.xml");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                orderService.cancel(p.userId1));
        assertEquals("Order with id="+p.userId1+" does not exist",
                exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void getByUserId_Ok() throws Exception {
        setupData("setup.base.orders.xml");
        var res = orderService.getByUserId(p.userId1);
        assertEquals(2, res.size());
        assertEquals("Vladimir", res.get(0).getUserName());
        assertEquals("Vladimir", res.get(1).getUserName());
        assertEquals("Moscow, Kremlin", res.get(0).getDestination());
        assertEquals("DC Washington", res.get(1).getDestination());
    }

    @Test
    void getByCourierId() throws Exception {
        setupData("setup.base.orders.xml");
        var res = orderService.getByCourierId(p.id3);
        assertEquals(1, res.size());
        assertEquals("Moscow, Kremlin", res.get(0).getDestination());
    }

    @Test
    void changeStatus() throws Exception {
        setupData("setup.base.orders.xml");
        orderService.changeStatus(p.id1, Status.DELIVERED);
        assertData("expected.change-status.ok.xml", EXCLUDED_COLS);
    }

    @Test
    void getAll() throws Exception {
        setupData("setup.base.orders.xml");
        var lst = orderService.getAll();
        assertEquals(2, lst.size());
    }

    @Test
    void assign() throws Exception {
        setupData("setup.base.orders.xml");
        orderService.assign(new AssignToCourierRequest(p.id2, p.id3, "Petrov", "+79991234568"));
        assertData("expected.assign.ok.xml", EXCLUDED_COLS);
    }

}