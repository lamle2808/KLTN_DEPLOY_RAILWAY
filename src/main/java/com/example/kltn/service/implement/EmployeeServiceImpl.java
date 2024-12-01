package com.example.kltn.service.implement;

import com.example.kltn.entity.Employee;
import com.example.kltn.repository.EmployeeRepo;
import com.example.kltn.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepo;

    @Override
    public Employee saveOrUpdate(Employee employee) {
        return employeeRepo.save(employee);
    }

    @Override
    public List<Employee> getAll() {
        return employeeRepo.findAll();
    }

    @Override
    public Employee getById(Long id) {
        return employeeRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepo.deleteById(id);
    }
} 