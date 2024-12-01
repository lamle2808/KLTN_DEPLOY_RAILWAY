package com.example.kltn.service.implement;

import com.example.kltn.entity.ServiceEvent;
import com.example.kltn.repository.ServiceRepo;
import com.example.kltn.service.ServiceEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ServiceEventServiceImpl implements ServiceEventService {

    private final ServiceRepo serviceRepo;

    @Override
    public ServiceEvent saveOrUpdate(ServiceEvent serviceEvent) {
        return serviceRepo.save(serviceEvent);
    }

    @Override
    public List<ServiceEvent> getAll() {
        return serviceRepo.findAll();
    }

    @Override
    public ServiceEvent getById(Long id) {
        return serviceRepo.findServiceEventById(id);
    }

    @Override
    public void deleteService(Long id) {
        serviceRepo.deleteById(id);
    }

    @Override
    public List<ServiceEvent> getByName(String name) {
        return serviceRepo.findServiceEventByServiceName(name);
    }
} 