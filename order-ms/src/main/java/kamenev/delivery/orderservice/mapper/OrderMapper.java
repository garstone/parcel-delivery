package kamenev.delivery.orderservice.mapper;

import kamenev.delivery.orderservice.domain.Order;
import kamenev.delivery.orderservice.dto.OrderDetails;
import kamenev.delivery.orderservice.dto.OrderDto;
import kamenev.delivery.orderservice.model.OrderCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order fromDto(OrderDto dto);

    @Mapping(source = "name", target = "userName")
    @Mapping(source = "phone", target = "userPhone")
    @Mapping(target = "status", constant = "CREATED")
    Order fromOrderCreateRequest(OrderCreateRequest request);

    OrderDto toDto(Order order);

    OrderDetails toOrderDetailsResponse(Order entity);

}
