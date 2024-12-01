package com.example.kltn.service.implement;

import com.example.kltn.entity.Customer;
import com.example.kltn.repository.CustomerRepo;
import com.example.kltn.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;

    @Override
    public Customer saveOrUpdate(Customer customer) {
        return customerRepo.save(customer);
    }

    @Override
    public List<Customer> getAll() {
        return customerRepo.findAll();
    }

    @Override
    public Customer getById(Long id) {
        return customerRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepo.deleteById(id);
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với email: " + email));
    }
} 