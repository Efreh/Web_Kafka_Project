package com.efr.OrderProcessingService.controller;

import com.efr.OrderProcessingService.exception.OrderNotFoundException;
import com.efr.OrderProcessingService.model.Order;
import com.efr.OrderProcessingService.model.OrderStatus;
import com.efr.OrderProcessingService.service.interfaces.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Эндпоинт для получения всех заказов с поддержкой пагинации
    @GetMapping
    public ResponseEntity<Page<Order>> getAllOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size); // Устанавливаем параметры по умолчанию
        Page<Order> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }


    // Эндпоинт для получения заказа по ID с выбрасыванием исключения
    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found"));
    }

    // Создать новый заказ
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody @Valid Order order) {
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    // Обновить статус заказа
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody @Valid String orderStatus) {
        OrderStatus status = OrderStatus.valueOf(orderStatus);
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    // Удалить заказ
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}