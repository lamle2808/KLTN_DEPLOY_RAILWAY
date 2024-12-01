package com.example.kltn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kltn.entity.ServiceCategory;
import java.util.List;

@Repository
public interface ServiceCategoryRepo extends JpaRepository<ServiceCategory, Long>{

    ServiceCategory findServiceCategoryById(Long id);

    ServiceCategory findServiceCategoryByName(String name);

    boolean existsByName(String name);

    List<ServiceCategory> findByNameContainingIgnoreCase(String keyword);

}
