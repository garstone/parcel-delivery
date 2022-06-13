package kamenev.delivery.orderservice.mapper;

import kamenev.delivery.orderservice.domain.Order;
import kamenev.delivery.orderservice.dto.OrderDto;
import kamenev.delivery.orderservice.dto.OrderDetails;
import kamenev.delivery.orderservice.model.OrderCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    public OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    public Order fromDto(OrderDto dto);

    public Order fromOrderCreateRequest(OrderCreateRequest request);

    public OrderDto toDto(Order order);

    public OrderDetails toOrderDetailsResponse(Order entity);

}
