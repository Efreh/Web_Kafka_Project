package com.efr.OrderNotificationsService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Обработчик исключений для не найденных заказов
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFound(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + ex.getMessage());
    }

    // Обработчик ошибок валидации
    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> handleValidationErrors(BindException ex) {
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMessage.toString());
    }

    // Обработчик всех других исключений
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }
}