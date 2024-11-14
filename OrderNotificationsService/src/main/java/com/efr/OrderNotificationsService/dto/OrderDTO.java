package com.efr.OrderNotificationsService.dto;

import com.efr.OrderNotificationsService.model.Order;
import com.efr.OrderNotificationsService.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderDTO {

    private Long orderId;
    private String orderStatus;

    // Преобразование сущности Order в DTO
    public static OrderDTO fromOrder(Order order) {
        return new OrderDTO(
                order.getOrderId(),
                order.getOrderStatus().name()  // Переводим enum в строку
        );
    }

    // Преобразование DTO в сущность Order
    public Order toOrder() {
        OrderStatus status = OrderStatus.valueOf(orderStatus);
        return new Order(orderId, status, null);
    }
}