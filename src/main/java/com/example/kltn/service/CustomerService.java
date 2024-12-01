package com.example.kltn.service;

import com.example.kltn.entity.Customer;
import java.util.List;

public interface CustomerService {
    Customer saveOrUpdate(Customer customer);
    List<Customer> getAll();
    Customer getById(Long id);
    void deleteCustomer(Long id);
    Customer findByEmail(String email);
} 