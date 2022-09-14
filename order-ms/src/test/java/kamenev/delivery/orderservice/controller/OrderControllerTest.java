package kamenev.delivery.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kamenev.delivery.orderservice.dto.OrderDetails;
import kamenev.delivery.orderservice.errors.ErrorResponse;
import kamenev.delivery.orderservice.model.ChangeDestinationRequest;
import kamenev.delivery.orderservice.model.OrderCreateRequest;
import kamenev.delivery.orderservice.service.IOrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration
@WebMvcTest(controllers = OrderController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private IOrderService orderService;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private final static String CHANGE_DESTINATION = "/api/orders/change-destination/";
    private final static String CREATE = "/api/orders/create/";
    private final static String CANCEL = "/api/orders/cancel/";

    @BeforeAll
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void create_Ok() throws Exception{
        var req = new OrderCreateRequest(UUID.randomUUID(), "+79991110011", "Vladimir", "Moscow, Kremlin", "Moscow", "");
        var orderDetails = new OrderDetails();
        orderDetails.setUserName("Vladimir");
        when(orderService.create(eq(req))).thenReturn(orderDetails);

        mockMvc.perform(put(CREATE)
                        .content(mapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andExpect(content().string(mapper.writeValueAsString(orderDetails)));
    }

    @Test
    void create_ThrowsRuntimeException() throws Exception {
        var req = new OrderCreateRequest(UUID.randomUUID(), "+79991110011", "Vladimir", "Moscow, Kremlin", "Moscow", "");
        var errResponse = new ErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        when(orderService.create(req)).thenThrow(new RuntimeException("ERROR"));

        mockMvc.perform(put(CREATE)
                        .content(mapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(mapper.writeValueAsString(errResponse)));
    }

    @Test
    void create_InvalidRequest() throws Exception {
        var req = new OrderCreateRequest(null, "1", "", "", "Moscow", "");

        mockMvc.perform(put(CREATE)
                        .content(mapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void changeDestination_Ok() throws Exception {
        var req = new ChangeDestinationRequest(UUID.randomUUID(), "Sochi, Mansion");
        var orderDetails = new OrderDetails();
        orderDetails.setUserName("Vladimir");
        when(orderService.changeDestination(any(), eq("Sochi, Mansion"))).thenReturn(orderDetails);

        mockMvc.perform(patch(CHANGE_DESTINATION)
                        .content(mapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(orderDetails)));
    }

    @Test
    void changeDestination_ThrowsResponseStatusException() throws Exception {
        var req = new ChangeDestinationRequest(UUID.randomUUID(), "Sochi, Mansion");
        var ex = new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Order with id=%s does not exist", "test"));
        when(orderService.changeDestination(any(), eq("Sochi, Mansion"))).thenThrow(ex);

        mockMvc.perform(patch(CHANGE_DESTINATION)
                        .content(mapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNotFound());
    }

    @Test
    void changeDestination_InvalidRequest() throws Exception {
        var req = new ChangeDestinationRequest(null, "");

        mockMvc.perform(patch(CHANGE_DESTINATION)
                        .content(mapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());

        verify(orderService, times(0)).changeDestination(any(), any());
    }

    @Test
    void cancel_Ok() throws Exception {
        doNothing().when(orderService).cancel(any());

        var res = mockMvc.perform(post(CANCEL + UUID.randomUUID())
                        .contentType(MediaType.ALL)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());

        verify(orderService, times(1)).cancel(any());
    }

    @Test
    void cancel_InvalidRequest() throws Exception {
        var res = mockMvc.perform(post(CANCEL + "abc")
                        .content("{'a': 'b'}")
                        .contentType(MediaType.ALL)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
        verify(orderService, times(0)).cancel(any());
    }

    // todo too lazy to test web layer more... maybe later

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