package com.example.orderservice.application.mapper;


import com.example.orderservice.domain.Order;
import com.example.orderservice.application.dto.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "quantity", target = "amount")  // Example mapping if 'amount' is derived from 'quantity'
    @Mapping(source = "orderDate", target = "orderDate")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "customerId", target = "customerId")
    OrderDTO orderToOrderDTO(Order order);

    @Mapping(source = "amount", target = "quantity")  // Reverse mapping example
    @Mapping(source = "orderDate", target = "orderDate")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "customerId", target = "customerId")
    Order orderDTOToOrder(OrderDTO orderDTO);
}


