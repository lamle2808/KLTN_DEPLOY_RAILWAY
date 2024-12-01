package com.example.kltn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Date;

import com.example.kltn.entity.Event;
import com.example.kltn.entity.EventCategory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface EventRepo extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    // Tìm kiếm theo nhiều tiêu chí
    @Query("SELECT e FROM Event e WHERE " +
           "(:categoryId IS NULL OR e.category.id = :categoryId) AND " +
           "(:locationId IS NULL OR e.eventLocation.id = :locationId) AND " +
           "(:minPrice IS NULL OR e.depositRequired >= :minPrice) AND " +
           "(:maxPrice IS NULL OR e.depositRequired <= :maxPrice) AND " +
           "(:keyword IS NULL OR LOWER(e.enventName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Event> searchEvents(
        @Param("categoryId") Long categoryId,
        @Param("locationId") Long locationId,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("keyword") String keyword,
        Pageable pageable
    );

    // Tìm events sắp diễn ra
    @Query("SELECT e FROM Event e WHERE e.enventDate > :now")
    Page<Event> findUpcomingEvents(@Param("now") Date now, Pageable pageable);

    // Tìm events phổ biến (dựa trên số lượng đơn đặt)
    @Query("SELECT e FROM Event e LEFT JOIN e.orders o GROUP BY e ORDER BY COUNT(o) DESC")
    Page<Event> findPopularEvents(Pageable pageable);
    Page<Event> findByCategoryAndIdNot(EventCategory category, Long eventId, Pageable pageable);

    Page<Event> findByEnventDateGreaterThanEqual(Date date, Pageable pageable);
}
