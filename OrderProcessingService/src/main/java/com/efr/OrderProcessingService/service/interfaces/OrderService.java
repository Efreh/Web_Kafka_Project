package com.efr.OrderProcessingService.service.interfaces;

import com.efr.OrderProcessingService.model.Order;
import com.efr.OrderProcessingService.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderService {

    Page<Order> getAllOrders(Pageable pageable);

    Optional<Order> getOrderById(Long id);

    Order createOrder(Order order);

    Order updateOrderStatus(Long id, OrderStatus orderStatus);

    void deleteOrder(Long id);
}
