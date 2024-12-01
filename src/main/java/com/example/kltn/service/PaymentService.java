package com.example.kltn.service;

import com.example.kltn.entity.Payment;
import java.util.List;

public interface PaymentService {
    Payment saveOrUpdate(Payment payment);
    List<Payment> getAll();
    Payment getById(Long id);
    void deletePayment(Long id);
} 