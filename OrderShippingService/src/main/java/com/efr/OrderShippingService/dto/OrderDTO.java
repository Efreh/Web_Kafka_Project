package com.efr.OrderShippingService.dto;

import com.efr.OrderShippingService.model.Order;
import com.efr.OrderShippingService.model.OrderStatus;
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
        try {
            OrderStatus status = OrderStatus.valueOf(orderStatus);

            if (status == OrderStatus.PAYMENT) {
                return new Order(orderId, OrderStatus.PACKAGE, null);
            } else {
                throw new IllegalArgumentException("Invalid order status: " + orderStatus);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to map order status: " + orderStatus, e);
        }
    }
}