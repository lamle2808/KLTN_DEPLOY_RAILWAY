package com.example.kltn.repository;

import com.example.kltn.entity.ImageEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ImageEventRepo extends JpaRepository<ImageEvent, Long> {
    
    @Query("SELECT i FROM ImageEvent i WHERE i.event.id = :eventId")
    List<ImageEvent> findByEventId(@Param("eventId") Long eventId);
    
    // Thêm các method khác nếu cần
    List<ImageEvent> findByEvent_Id(Long eventId); // Cách viết khác sử dụng method naming
    
    void deleteByEvent_Id(Long eventId);
    
    boolean existsByEvent_Id(Long eventId);
} 
