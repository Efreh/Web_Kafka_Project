package com.efr.OrderShippingService.service;

import com.efr.OrderShippingService.dto.OrderDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.producer.topic}")
    private String topic;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(OrderDTO orderDTO) {
        try {
            String message = objectMapper.writeValueAsString(orderDTO);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
            // Ожидание завершения отправки сообщения
            future.join(); // Блокирует выполнение до получения результата
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing message", e);
        }
    }
}