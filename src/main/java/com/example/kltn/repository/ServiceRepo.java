package com.example.kltn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.kltn.entity.ServiceEvent;

@Repository
public interface ServiceRepo extends JpaRepository<ServiceEvent, Long> {

    ServiceEvent findServiceEventById(Long id);

    @Query("SELECT u FROM ServiceEvent u WHERE u.servicename LIKE CONCAT(:name, '%')")
    List<ServiceEvent> findServiceEventByServiceName(@Param("name") String name);

}
