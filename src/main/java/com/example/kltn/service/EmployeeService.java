package com.example.kltn.service;

import com.example.kltn.entity.Employee;
import java.util.List;

public interface EmployeeService {
    Employee saveOrUpdate(Employee employee);
    List<Employee> getAll();
    Employee getById(Long id);
    void deleteEmployee(Long id);
} 