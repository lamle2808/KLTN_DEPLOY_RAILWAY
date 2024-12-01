package com.example.kltn.service.implement;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.example.kltn.entity.ServiceCategory;
import com.example.kltn.repository.ServiceCategoryRepo;
import com.example.kltn.service.ServiceCategoryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@SessionScope
public class ServiceCategoryImpl implements ServiceCategoryService {
    private final ServiceCategoryRepo serviceCategoryRepo;

    @Override
    public ServiceCategory saveOrUpdate(ServiceCategory serviceCategory) {
        return serviceCategoryRepo.save(serviceCategory);
    }

    @Override
    public void delServiceCategory(Long id) {
        serviceCategoryRepo.delete(serviceCategoryRepo.findServiceCategoryById(id));
    }

    @Override
    public List<ServiceCategory> getAll() {
        return serviceCategoryRepo.findAll();
    }

    @Override
    public ServiceCategory getByName(String name) {
        return serviceCategoryRepo.findServiceCategoryByName(name);
    }

    @Override
    public ServiceCategory getById(Long id) {
        return serviceCategoryRepo.findServiceCategoryById(id);
    }

    @Override
    public boolean checkExistsByName(String name) {
        return serviceCategoryRepo.existsByName(name);
    }

    @Override
    public List<ServiceCategory> searchByKeyword(String keyword) {
        return serviceCategoryRepo.findByNameContainingIgnoreCase(keyword);
    }

}
