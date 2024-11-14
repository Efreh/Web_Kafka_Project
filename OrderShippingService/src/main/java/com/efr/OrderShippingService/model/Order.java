package com.efr.OrderShippingService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "shipping_orders")
public class Order {

    @Id
    private Long orderId;
    private OrderStatus orderStatus;
    private LocalDate orderShippingDate;

    public void updateStatus(OrderStatus newStatus) {
        this.orderStatus = newStatus;
    }
}