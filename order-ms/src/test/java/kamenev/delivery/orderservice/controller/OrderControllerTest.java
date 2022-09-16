package kamenev.delivery.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kamenev.delivery.orderservice.domain.Status;
import kamenev.delivery.orderservice.dto.OrderDetails;
import kamenev.delivery.orderservice.dto.OrderDto;
import kamenev.delivery.orderservice.errors.ErrorResponse;
import kamenev.delivery.orderservice.model.AssignToCourierRequest;
import kamenev.delivery.orderservice.model.ChangeDestinationRequest;
import kamenev.delivery.orderservice.model.ChangeStatusRequest;
import kamenev.delivery.orderservice.model.OrderCreateRequest;
import kamenev.delivery.orderservice.service.IOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class OrderControllerTest {

    private final static String DETAILS = "/api/orders/details/";
    private final static String CHANGE_DESTINATION = "/api/orders/change-destination";
    private final static String CREATE = "/api/orders/create";
    private final static String CANCEL = "/api/orders/cancel/";
    private final static String GET_BY_USER_ID = "/api/orders/user/";
    private final static String GET_BY_COURIER_ID = "/api/orders/courier/";
    private final static String CHANGE_STATUS = "/api/orders/change-status";
    private final static String ALL = "/api/orders/all";
    private final static String ASSIGN = "/api/orders/assign";
    OrderDto orderDto2 = new OrderDto(UUID.randomUUID(), null, "bcd", null, null, null, null, 0, null, null, null, null, null, null, null, null);
    OrderDto orderDto1 = new OrderDto(UUID.randomUUID(), null, "abc", null, null, null, null, 1, null, null, null, null, null, null, null, null);
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private IOrderService orderService;
    @Autowired
    private MockMvc mockMvc;


    @Test
    void create_Ok() throws Exception {
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

    @Test
    void getDetails() throws Exception {
        var id = UUID.randomUUID();

        var details = new OrderDetails();
        details.setUserName("username");
        details.setUserPhone("123");

        when(orderService.get(id)).thenReturn(details);

        var res = mockMvc.perform(get(DETAILS + id))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(details)));

        verify(orderService, times(1)).get(id);
    }

    @Test
    void getAllByUser() throws Exception {
        var details1 = new OrderDetails();
        details1.setUserName("username");
        details1.setUserPhone("123");
        var details2 = new OrderDetails();
        details2.setUserName("username2");
        details2.setUserPhone("1232222");
        var lst = List.of(details1, details2);
        var id = UUID.randomUUID();

        when(orderService.getByUserId(id)).thenReturn(lst);

        var res = mockMvc.perform(get(GET_BY_USER_ID + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(lst)));

        verify(orderService, times(1)).getByUserId(id);
    }

    @Test
    void getAllByUser_NoOrders() throws Exception {
        when(orderService.getByUserId(any())).thenReturn(List.of());

        var res = mockMvc.perform(get(GET_BY_USER_ID + UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        verify(orderService, times(1)).getByUserId(any());
    }

    @Test
    void getAllByCourier() throws Exception {
        var details1 = new OrderDetails();
        details1.setUserName("username");
        details1.setUserPhone("123");
        var details2 = new OrderDetails();
        details2.setUserName("username2");
        details2.setUserPhone("1232222");
        var lst = List.of(details1, details2);
        var id = UUID.randomUUID();

        when(orderService.getByCourierId(id)).thenReturn(lst);

        var res = mockMvc.perform(get(GET_BY_COURIER_ID + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(lst)));

        verify(orderService, times(1)).getByCourierId(id);
    }

    @Test
    void changeStatus() throws Exception {
        var id = UUID.randomUUID();
        var changeStatusRequest = new ChangeStatusRequest(id, Status.COURIER_WAITING);

        when(orderService.changeStatus(id, Status.COURIER_WAITING)).thenReturn(orderDto1);

        mockMvc.perform(patch(CHANGE_STATUS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(changeStatusRequest)))
                .andExpect(status().isOk());

        verify(orderService, times(1)).changeStatus(id, Status.COURIER_WAITING);
    }

    @Test
    void getAll() throws Exception {
        var lst = List.of(orderDto1, orderDto2);

        when(orderService.getAll()).thenReturn(lst);

        mockMvc.perform(get(ALL))
                .andExpectAll(content().contentType(MediaType.APPLICATION_JSON),
                        content().json(mapper.writeValueAsString(lst)));

        verify(orderService, times(1)).getAll();

    }

    @Test
    void assign() throws Exception {
        var req = new AssignToCourierRequest(UUID.randomUUID(), UUID.randomUUID(), "Petya");

        when(orderService.assign(req)).thenReturn(orderDto1);

        mockMvc.perform(post(ASSIGN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpectAll(status().isOk());

        verify(orderService, times(1)).assign(req);
    }

}