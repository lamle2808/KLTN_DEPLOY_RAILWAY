package com.example.kltn.service;

import com.example.kltn.entity.ServiceEvent;
import java.util.List;

public interface ServiceEventService {
    ServiceEvent saveOrUpdate(ServiceEvent serviceEvent);
    List<ServiceEvent> getAll();
    ServiceEvent getById(Long id);
    void deleteService(Long id);
    List<ServiceEvent> getByName(String name);
} 