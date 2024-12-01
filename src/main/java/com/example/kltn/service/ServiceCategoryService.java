package com.example.kltn.service;

import com.example.kltn.entity.ServiceCategory;
import java.util.*;
public interface ServiceCategoryService {
    ServiceCategory saveOrUpdate(ServiceCategory serviceCategory);
    void delServiceCategory(Long id);
    List<ServiceCategory> getAll();
    ServiceCategory getByName(String name);
    ServiceCategory getById(Long id);
    boolean checkExistsByName(String name);
    List<ServiceCategory> searchByKeyword(String keyword);
}
