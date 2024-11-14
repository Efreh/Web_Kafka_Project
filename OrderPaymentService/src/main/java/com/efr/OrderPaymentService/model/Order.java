package com.efr.OrderPaymentService.model;

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
@Entity(name = "pay_orders")
public class Order {

    @Id
    private Long orderId;
    private OrderStatus orderStatus;
    private LocalDate orderPaymentDate;

    public void updateStatus(OrderStatus newStatus) {
        this.orderStatus = newStatus;
    }
}