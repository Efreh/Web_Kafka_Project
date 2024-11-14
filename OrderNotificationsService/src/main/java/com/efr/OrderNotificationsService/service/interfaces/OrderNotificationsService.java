package com.efr.OrderNotificationsService.service.interfaces;

import com.efr.OrderNotificationsService.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderNotificationsService {
    void sendOrderDeliveredNotification(Long orderId);

    Page<Order> getAllOrders(Pageable pageable);
}
