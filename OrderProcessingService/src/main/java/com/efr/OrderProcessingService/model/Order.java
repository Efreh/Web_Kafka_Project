package com.efr.OrderProcessingService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "'orders'")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @NotNull(message = "Customer ID must not be null.")
    private Long customerId;

    private OrderStatus orderStatus = OrderStatus.NEW;

    private LocalDate orderDate = LocalDate.now();

    @NotNull(message = "Total pri ce must not be null.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than zero.")
    private BigDecimal totalPrice;

    @NotNull(message = "Product name must not be null.")
    @NotEmpty(message = "Product name must not be empty.")
    private String product;

    @NotNull(message = "Product count must not be null.")
    @Min(value = 1, message = "Product count must be at least 1.")
    private int productCount;

    public Order(BigDecimal totalPrice, String product, int productCount) {
        this.totalPrice = totalPrice;
        this.product = product;
        this.productCount = productCount;
    }

    public void updateStatus(OrderStatus newStatus) {
        this.orderStatus = newStatus;
    }
}
