package com.example.kltn.service;

import com.example.kltn.entity.Order;
import com.example.kltn.entity.Employee;
import java.util.List;

public interface OrderService {
    Order saveOrUpdate(Order order);
    List<Order> getAll();
    Order getById(Long id);
    void deleteOrder(Long id);
    void updateOrderStatus(Long orderId, String status);
    void assignEmployeeToOrder(Long orderId, Employee employee);
} 