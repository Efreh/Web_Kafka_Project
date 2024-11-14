package com.efr.OrderNotificationsService.repository;

import com.efr.OrderNotificationsService.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
