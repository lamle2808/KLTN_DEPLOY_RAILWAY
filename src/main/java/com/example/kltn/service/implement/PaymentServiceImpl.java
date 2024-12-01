package com.example.kltn.service.implement;

import com.example.kltn.entity.Payment;
import com.example.kltn.repository.PaymentRepo;
import com.example.kltn.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;

    @Override
    public Payment saveOrUpdate(Payment payment) {
        return paymentRepo.save(payment);
    }

    @Override
    public List<Payment> getAll() {
        return paymentRepo.findAll();
    }

    @Override
    public Payment getById(Long id) {
        return paymentRepo.findById(id).orElse(null);
    }

    @Override
    public void deletePayment(Long id) {
        paymentRepo.deleteById(id);
    }
} 