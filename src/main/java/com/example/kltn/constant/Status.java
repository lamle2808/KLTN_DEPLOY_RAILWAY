package com.example.kltn.constant;

public enum Status {
    // Payment status
    PAYMENT_PENDING("PENDING"),
    PAYMENT_PROCESSING("PROCESSING"),
    PAYMENT_SUCCESS("SUCCESS"), 
    PAYMENT_FAILED("FAILED"),
    PAYMENT_CANCELLED("CANCELLED"),

    // Order status  
    ORDER_PENDING("PENDING"),
    ORDER_PROCESSING("PROCESSING"),
    ORDER_COMPLETED("COMPLETED"),
    ORDER_CANCELLED("CANCELLED"),
    ORDER_PAID("PAID");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
} 