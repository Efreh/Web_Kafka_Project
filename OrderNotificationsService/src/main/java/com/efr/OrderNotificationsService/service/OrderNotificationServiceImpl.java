package com.efr.OrderNotificationsService.service;

import com.efr.OrderNotificationsService.model.Order;
import com.efr.OrderNotificationsService.repository.OrderRepository;
import com.efr.OrderNotificationsService.service.interfaces.OrderNotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderNotificationServiceImpl implements OrderNotificationsService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderNotificationServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void sendOrderDeliveredNotification(Long orderId) {

        System.out.println("Sending notification for order " + orderId + " delivered successfully");
        // Здесь можно использовать компоненты для отправки уведомлений
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}
