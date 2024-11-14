package com.efr.OrderProcessingService.service;

import com.efr.OrderProcessingService.dto.OrderDTO;
import com.efr.OrderProcessingService.exception.OrderNotFoundException;
import com.efr.OrderProcessingService.model.Order;
import com.efr.OrderProcessingService.model.OrderStatus;
import com.efr.OrderProcessingService.repository.OrderRepository;
import com.efr.OrderProcessingService.service.interfaces.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, KafkaProducerService kafkaProducerService) {
        this.orderRepository = orderRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        kafkaProducerService.sendMessage(OrderDTO.fromOrder(savedOrder));
        return savedOrder;
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long id, OrderStatus orderStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(id);

        if (!optionalOrder.isPresent()) {
            throw new OrderNotFoundException("Order with ID " + id + " not found.");
        }

        Order order = optionalOrder.get();

        order.updateStatus(orderStatus);

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
