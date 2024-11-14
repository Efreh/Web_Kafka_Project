package com.efr.OrderNotificationsService.service.interfaces;

import com.efr.OrderNotificationsService.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderDeliveredService {

    void processDelivered(Long orderId);

    Page<Order> getAllOrders(Pageable pageable);

    Optional<Order> getOrderById(Long id);
}
