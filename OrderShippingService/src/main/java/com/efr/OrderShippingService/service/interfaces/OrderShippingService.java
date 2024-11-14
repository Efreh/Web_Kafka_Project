package com.efr.OrderShippingService.service.interfaces;

import com.efr.OrderShippingService.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderShippingService {

    void processPackagingAndShipped(Long orderId);

    Page<Order> getAllOrders(Pageable pageable);

    Optional<Order> getOrderById(Long id);
}
