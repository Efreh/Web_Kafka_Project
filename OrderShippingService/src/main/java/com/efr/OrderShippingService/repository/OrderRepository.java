package com.efr.OrderShippingService.repository;

import com.efr.OrderShippingService.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
