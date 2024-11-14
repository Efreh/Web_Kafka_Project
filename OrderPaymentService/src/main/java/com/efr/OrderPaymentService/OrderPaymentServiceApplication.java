package com.efr.OrderPaymentService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OrderPaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderPaymentServiceApplication.class, args);
	}

}
