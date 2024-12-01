package com.example.kltn.service.implement;

import com.example.kltn.entity.Order;
import com.example.kltn.repository.OrderRepo;
import com.example.kltn.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;

    @Override
    public Order saveOrUpdate(Order order) {
        return orderRepo.save(order);
    }

    @Override
    public List<Order> getAll() {
        return orderRepo.findAll();
    }

    @Override
    public Order getById(Long id) {
        return orderRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepo.deleteById(id);
    }
} 