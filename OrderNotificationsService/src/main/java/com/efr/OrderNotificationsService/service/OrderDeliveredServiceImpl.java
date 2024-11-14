package com.efr.OrderNotificationsService.service;

import com.efr.OrderNotificationsService.exception.OrderNotFoundException;
import com.efr.OrderNotificationsService.model.Order;
import com.efr.OrderNotificationsService.model.OrderStatus;
import com.efr.OrderNotificationsService.repository.OrderRepository;
import com.efr.OrderNotificationsService.service.interfaces.OrderDeliveredService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class OrderDeliveredServiceImpl implements OrderDeliveredService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderDeliveredServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public void processDelivered(Long orderId) {
        Order order = getOrderById(orderId).orElseThrow(
                () -> new OrderNotFoundException("Order " + orderId + " not found")
        );

        order.updateStatus(OrderStatus.DELIVERED);
        order.setOrderDeliveredDate(LocalDate.now());

        orderRepository.save(order);

        System.out.println("The order " + order.getOrderId() + " has been successfully delivered");
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
}
