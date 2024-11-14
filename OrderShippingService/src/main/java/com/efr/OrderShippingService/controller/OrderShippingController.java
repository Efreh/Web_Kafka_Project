package com.efr.OrderShippingService.controller;

import com.efr.OrderShippingService.exception.OrderNotFoundException;
import com.efr.OrderShippingService.model.Order;
import com.efr.OrderShippingService.service.interfaces.OrderShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/shipped")
public class OrderShippingController {

    private final OrderShippingService orderShippingService;

    @Autowired
    public OrderShippingController(OrderShippingService orderShippingService) {
        this.orderShippingService = orderShippingService;
    }

    // Эндпоинт для упаковки и отправки заказа
    @PostMapping
    public ResponseEntity<String> processPackageAndShipped(@RequestBody Long orderId) {
        orderShippingService.processPackagingAndShipped(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("Shipping processed successfully for order " + orderId);
    }

    // Эндпоинт для получения всех заказов с поддержкой пагинации
    @GetMapping
    public ResponseEntity<Page<Order>> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderShippingService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    // Эндпоинт для получения заказа по ID
    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable Long orderId) {
        return orderShippingService.getOrderById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found"));
    }
}
