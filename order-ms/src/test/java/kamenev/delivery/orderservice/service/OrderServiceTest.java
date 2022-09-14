package kamenev.delivery.orderservice.service;

import kamenev.delivery.orderservice.integration.AbstractDbTest;
import kamenev.delivery.orderservice.model.OrderCreateRequest;
import kamenev.delivery.orderservice.repository.OrderRepository;
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
    private OrderRepository orderRepository;

    @Autowired
    private IOrderService orderService;

//    @PersistenceContext
//    private EntityManager entityManager;

//    @Autowired
//    private ApplicationContext context;

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