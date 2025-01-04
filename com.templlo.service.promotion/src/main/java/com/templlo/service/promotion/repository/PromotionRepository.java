package com.templlo.service.promotion.repository;

import com.templlo.service.promotion.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, UUID> {
    @Query("SELECT p FROM Promotion p " +
            "WHERE (:type IS NULL OR p.type = :type) " +
            "AND (:status IS NULL OR p.status = :status)")
    Page<Promotion> findByFilters(@Param("type") String type,
                                  @Param("status") String status,
                                  Pageable pageable);
}
