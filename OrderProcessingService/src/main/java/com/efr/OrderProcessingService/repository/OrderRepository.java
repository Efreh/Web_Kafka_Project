package com.efr.OrderProcessingService.repository;

import com.efr.OrderProcessingService.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
