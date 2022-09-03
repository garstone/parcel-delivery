package kamenev.delivery.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kamenev.delivery.orderservice.dto.OrderDetails;
import kamenev.delivery.orderservice.errors.ErrorResponse;
import kamenev.delivery.orderservice.model.OrderCreateRequest;
import kamenev.delivery.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest(classes = OrderController.class)
//@AutoConfigureMockMvc
//@EnableWebMvc
@ContextConfiguration
@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private OrderController orderController;

    @MockBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Test
    void create_Ok() throws Exception{
        var req = new OrderCreateRequest(UUID.randomUUID(), "+79991110011", "Vladimir", "Moscow, Kremlin", "");
        var orderDetails = new OrderDetails();
        orderDetails.setUserName("Vladimir");
        when(orderService.create(eq(req))).thenReturn(orderDetails);

        mockMvc.perform(put("/api/orders/create")
                        .content(mapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andExpect(content().string(mapper.writeValueAsString(orderDetails)));
    }

    @Test
    void create_ThrowsRuntimeException() throws Exception {
        var req = new OrderCreateRequest(UUID.randomUUID(), "+79991110011", "Vladimir", "Moscow, Kremlin", "");
        var errResponse = new ErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        when(orderService.create(req)).thenThrow(new RuntimeException("ERROR"));

        mockMvc.perform(put("/api/orders/create")
                        .content(mapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(mapper.writeValueAsString(errResponse)));
    }

    @Test
    void create_InvalidRequest() throws Exception {
        var req = new OrderCreateRequest(null, "1", "", "", "");

        var contextMockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        contextMockMvc.perform(put("/api/orders/create")
                        .content(mapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void changeDestination_Ok() {

    }

    @Test
    void changeDestination_Throws() {

    }

    @Test
    void cancel() {
    }

    @Test
    void getDetails() {
    }

    @Test
    void getAllByUser() {
    }

    @Test
    void getAllByCourier() {
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
    void track() {
    }
}