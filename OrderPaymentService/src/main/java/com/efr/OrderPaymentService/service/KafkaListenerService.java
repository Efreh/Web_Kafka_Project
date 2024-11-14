package com.efr.OrderPaymentService.service;

import com.efr.OrderPaymentService.dto.OrderDTO;
import com.efr.OrderPaymentService.model.Order;
import com.efr.OrderPaymentService.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class KafkaListenerService {

    private final OrderRepository orderRepository;

    @Autowired
    public KafkaListenerService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Retryable(
            retryFor = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @KafkaListener(
            topics = "${spring.kafka.consumer.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            concurrency = "${spring.kafka.listener.concurrency}"  // Число потоков для обработки сообщений
    )
    public void listenNewOrder(String message, Acknowledgment acknowledgment) throws Exception {
        // Десериализуем сообщение в DTO
        OrderDTO orderDTO = new ObjectMapper().readValue(message, OrderDTO.class);

        // Проверяем, существует ли уже заказ с таким идентификатором
        if (orderRepository.existsById(orderDTO.getOrderId())) {
            acknowledgment.acknowledge();
            return;
        }

        // Преобразуем DTO в сущность Order и сохраняем его в базу данных
        Order order = orderDTO.toOrder();
        orderRepository.save(order);

        acknowledgment.acknowledge();
    }
}