package com.efr.OrderPaymentService.model;

public enum OrderStatus {
    NEW("Новый заказ"), // Новый заказ, который еще не обработан
    PENDING("Ожидает оплаты"), // Заказ ожидает оплаты
    PAYMENT("Оплачен"), // Заказ оплачен
    PACKAGE("Упаковка"), // Заказ на упаковке
    SHIPPED("Отправлен"), // Заказ отправлен покупателю
    DELIVERED("Доставлен"), // Заказ доставлен покупателю
    CANCELED("Отменен"); // Заказ отменен

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
