package com.efr.OrderNotificationsService.controller;

import com.efr.OrderNotificationsService.exception.OrderNotFoundException;
import com.efr.OrderNotificationsService.model.Order;
import com.efr.OrderNotificationsService.service.interfaces.OrderDeliveredService;
import com.efr.OrderNotificationsService.service.interfaces.OrderNotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/delivered")
public class OrderDeliveredController {

    private final OrderDeliveredService orderDeliveredService;
    private final OrderNotificationsService orderNotificationsService;

    @Autowired
    public OrderDeliveredController(OrderDeliveredService orderDeliveredService, OrderNotificationsService orderNotificationsService) {
        this.orderDeliveredService = orderDeliveredService;
        this.orderNotificationsService = orderNotificationsService;
    }

    // Эндпоинт для подтверждения доставки и уведомления
    @PostMapping
    public ResponseEntity<String> processDeliveredAndNotifications(@RequestBody Long orderId) {
        try {
            orderDeliveredService.processDelivered(orderId);
            orderNotificationsService.sendOrderDeliveredNotification(orderId);

            return ResponseEntity.status(HttpStatus.OK).body("Shipping processed successfully for order " + orderId);

        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found: " + orderId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process order " + orderId + ": " + e.getMessage());
        }
    }

    // Эндпоинт для получения всех заказов с поддержкой пагинации
    @GetMapping
    public ResponseEntity<Page<Order>> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderDeliveredService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }
}
