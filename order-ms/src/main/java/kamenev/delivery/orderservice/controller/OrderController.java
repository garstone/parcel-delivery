package kamenev.delivery.orderservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kamenev.delivery.orderservice.dto.OrderDetails;
import kamenev.delivery.orderservice.dto.OrderDto;
import kamenev.delivery.orderservice.model.*;
import kamenev.delivery.orderservice.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/orders")
@Validated
public class OrderController {

    private final IOrderService service;

    @PutMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDetails> create(@Valid @RequestBody OrderCreateRequest request) {
        OrderDetails response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping(value = "/change-destination", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDetails> changeDestination(@Valid @RequestBody ChangeDestinationRequest request) {
        OrderDetails response = service.changeDestination(request.id(), request.destination());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel/{id}")
    public HttpStatus cancel(@NotNull @PathVariable UUID id) {
        service.cancel(id);
        return HttpStatus.OK;
    }

    @GetMapping(value = "/details/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDetails> getDetails(@NotNull @PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping(value = "/user/{userId}", produces = APPLICATION_JSON_VALUE)
    public List<OrderDetails> getAllByUser(@NotNull @PathVariable UUID userId) {
        return service.getByUserId(userId);
    }

    @GetMapping(value = "/courier/{courierId}", produces = APPLICATION_JSON_VALUE)
    public List<OrderDetails> getAllByCourier(@NotNull @PathVariable UUID courierId) {
        return service.getByCourierId(courierId);
    }

    @PatchMapping(value = "/change-status", consumes = APPLICATION_JSON_VALUE)
    public HttpStatus changeStatus(@NotNull @RequestBody ChangeStatusRequest changeStatusRequest) {
        service.changeStatus(changeStatusRequest.orderId(), changeStatusRequest.status());
        return HttpStatus.OK;
    }

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public List<OrderDto> getAll() {
        return service.getAll();
    }

    @PostMapping(value = "/assign", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public HttpStatus assign(@NotNull @RequestBody AssignToCourierRequest request) {
        service.assign(request);
        return HttpStatus.OK;
    }

    @GetMapping(value = "/track/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TrackResponse> track(@NotNull @PathVariable UUID id) {
        return ResponseEntity.ok(service.getCoordinates(id));
    }

}
