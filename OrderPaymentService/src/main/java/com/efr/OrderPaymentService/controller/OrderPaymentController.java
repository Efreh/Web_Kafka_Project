package com.efr.OrderPaymentService.controller;

import com.efr.OrderPaymentService.exception.OrderNotFoundException;
import com.efr.OrderPaymentService.model.Order;
import com.efr.OrderPaymentService.service.interfaces.OrderPaymentService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/payment")
public class OrderPaymentController {

    private final OrderPaymentService orderPaymentService;

    @Autowired
    public OrderPaymentController(OrderPaymentService orderPaymentService) {
        this.orderPaymentService = orderPaymentService;
    }

    // Эндпоинт для процесса оплаты
    @PostMapping
    public ResponseEntity<String> processPayment(@RequestBody Long orderId) {
        orderPaymentService.processPayment(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("Payment processed successfully for order " + orderId);
    }

    // Эндпоинт для получения всех заказов с поддержкой пагинации
    @GetMapping
    public ResponseEntity<Page<Order>> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderPaymentService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    // Эндпоинт для получения заказа по ID
    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable Long orderId) {
        return orderPaymentService.getOrderById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found"));
    }
}
