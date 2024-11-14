package com.efr.OrderPaymentService.service;

import com.efr.OrderPaymentService.dto.OrderDTO;
import com.efr.OrderPaymentService.exception.OrderNotFoundException;
import com.efr.OrderPaymentService.model.Order;
import com.efr.OrderPaymentService.model.OrderStatus;
import com.efr.OrderPaymentService.repository.OrderRepository;
import com.efr.OrderPaymentService.service.interfaces.OrderPaymentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class OrderPaymentServiceImpl implements OrderPaymentService {

    private final OrderRepository orderRepository;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public OrderPaymentServiceImpl(OrderRepository orderRepository, KafkaProducerService kafkaProducerService) {
        this.orderRepository = orderRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    @Async
    @Transactional
    public void processPayment(Long orderId) {
        Order order = getOrderById(orderId).orElseThrow(
                () -> new OrderNotFoundException("Order " + orderId + " not found")
        );

        order.updateStatus(OrderStatus.PAYMENT);
        order.setOrderPaymentDate(LocalDate.now());
        orderRepository.save(order);
        kafkaProducerService.sendMessage(OrderDTO.fromOrder(order));
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
