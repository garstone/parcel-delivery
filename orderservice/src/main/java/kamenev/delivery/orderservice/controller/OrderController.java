package kamenev.delivery.orderservice.controller;

import kamenev.delivery.orderservice.dto.OrderDto;
import kamenev.delivery.orderservice.model.*;
import kamenev.delivery.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    private final OrderService service;

    @PutMapping
    public ResponseEntity<OrderDetailsResponse> create(@Valid @RequestBody OrderDto orderDTO) {
        OrderDetailsResponse response = service.create(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/change-destination")
    public ResponseEntity<OrderDetailsResponse> changeDestination(@Valid @RequestBody ChangeDestinationRequest request) {
        OrderDetailsResponse response = service.changeDestination(request.id(), request.destination());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel/{id}")
    public HttpStatus cancel(@NotNull @PathVariable UUID id) {
        service.cancel(id);
        return HttpStatus.OK;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailsResponse> getDetails(@NotNull @PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping("/user/{userId}")
    public List<OrderDetailsResponse> getAllByUser(@NotNull @PathVariable UUID userId) {
        return service.getByUserId(userId);
    }

    @GetMapping("/courier/{courierId}")
    public List<OrderDetailsResponse> getAllByCourier(@NotNull @PathVariable UUID courierId) {
        return service.getByCourierId(courierId);
    }

    @PatchMapping("/change-status")
    public HttpStatus changeStatus(@NotNull @RequestBody ChangeStatusRequest changeStatusRequest) {
        service.changeStatus(changeStatusRequest.orderId(), changeStatusRequest.status());
        return HttpStatus.OK;
    }

    @GetMapping
    public List<OrderDto> getAll() {
        return service.getAll();
    }

    @PostMapping("/assign")
    public HttpStatus assign(@NotNull @RequestBody AssignToCourierRequest request) {
        service.assign(request);
        return HttpStatus.OK;
    }

    @GetMapping("/track/{id}")
    public ResponseEntity<TrackResponse> track(@NotNull @PathVariable UUID id) {
        return ResponseEntity.ok(service.getCoordinates(id));
    }

}
