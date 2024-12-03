package com.example.kltn.service;

import java.util.Map;

public interface VnpayService {
    String createPayment(String orderId, String amount, String orderInfo);
    boolean processPaymentNotify(Map<String, String> notifyParams);
    String hmacSHA512(String key, Map<String, String> params);
}
