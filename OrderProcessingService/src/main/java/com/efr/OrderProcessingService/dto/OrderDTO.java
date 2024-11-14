package com.efr.OrderProcessingService.dto;

import com.efr.OrderProcessingService.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderDTO {

    private Long orderId;
    private String orderStatus;

    public static OrderDTO fromOrder(Order order) {
        return new OrderDTO(
                order.getOrderId(),
                order.getOrderStatus().name()  // Переводим enum в строку
        );
    }
}
