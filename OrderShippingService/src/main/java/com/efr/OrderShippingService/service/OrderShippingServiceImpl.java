package com.efr.OrderShippingService.service;

import com.efr.OrderShippingService.dto.OrderDTO;
import com.efr.OrderShippingService.exception.OrderNotFoundException;
import com.efr.OrderShippingService.model.Order;
import com.efr.OrderShippingService.model.OrderStatus;
import com.efr.OrderShippingService.repository.OrderRepository;
import com.efr.OrderShippingService.service.interfaces.OrderShippingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class OrderShippingServiceImpl implements OrderShippingService {

    private final OrderRepository orderRepository;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public OrderShippingServiceImpl(OrderRepository orderRepository, KafkaProducerService kafkaProducerService) {
        this.orderRepository = orderRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    @Async
    @Transactional  // Делается в рамках транзакции
    public void processPackagingAndShipped(Long orderId) {
        Order order = getOrderById(orderId).orElseThrow(
                () -> new OrderNotFoundException("Order " + orderId + " not found")
        );

        order.updateStatus(OrderStatus.SHIPPED);
        order.setOrderShippingDate(LocalDate.now());
        orderRepository.save(order);
        kafkaProducerService.sendMessage(OrderDTO.fromOrder(order));
        System.out.println("The order "+ order.getOrderId() +" has been successfully packed and shipped");
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
