package kamenev.delivery.orderservice.mapper;

import kamenev.delivery.orderservice.domain.Order;
import kamenev.delivery.orderservice.dto.OrderDto;
import kamenev.delivery.orderservice.model.OrderDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    public OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    public Order fromDto(OrderDto dto);

    public OrderDto toDto(Order order);

    public OrderDetailsResponse toOrderDetailsResponse(Order entity);

}
