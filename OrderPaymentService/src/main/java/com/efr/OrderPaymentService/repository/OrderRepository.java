package com.efr.OrderPaymentService.repository;

import com.efr.OrderPaymentService.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
