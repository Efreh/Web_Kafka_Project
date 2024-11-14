package com.efr.OrderPaymentService.service.interfaces;

import com.efr.OrderPaymentService.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderPaymentService {

    void processPayment(Long orderId);

    Page<Order> getAllOrders(Pageable pageable);

    Optional<Order> getOrderById(Long id);
}
