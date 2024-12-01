package com.example.kltn.service;

import com.example.kltn.entity.Order;
import java.util.List;

public interface OrderService {
    Order saveOrUpdate(Order order);
    List<Order> getAll();
    Order getById(Long id);
    void deleteOrder(Long id);
} 