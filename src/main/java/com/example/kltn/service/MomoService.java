package com.example.kltn.service;

import java.util.Map;

public interface MomoService {
    String createPayment(String orderId, String amount, String orderInfo);
    boolean processPaymentNotify(Map<String, String> notifyParams);
} 