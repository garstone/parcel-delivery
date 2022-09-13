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
    private OrderService orderService;

//    @PersistenceContext
//    private EntityManager entityManager;

//    @Autowired
//    private ApplicationContext context;

    private static final String[] EXCLUDED_COLS = {"id", "created_at", "updated_at"};

    @Test
    void create_Ok() throws Exception {
        var orderCreateReq = new OrderCreateRequest(p.id1, "+79991234567", "Vladimir",
                "Moscow, Kremlin", "comments");
        var res = orderService.create(orderCreateReq);
        assertData("expected.create.ok.xml", EXCLUDED_COLS);
    }

    @Test
    void changeDestination_Ok() throws Exception {
        setupData("setup.change_destination.ok.xml");
        var res = orderService.changeDestination(p.id1, "New destination");
        assertData("expected.change_destination.ok.xml", EXCLUDED_COLS);
    }

    @Test
    void changeDestination_OrderNotExists() throws Exception {
        setupData("setup.change_destination.ok.xml");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                orderService.changeDestination(p.id2, "New destination"));
        assertEquals("Order with id="+p.id2+" does not exist",
                exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
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