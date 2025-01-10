package com.templlo.service.temple.repository;

import com.templlo.service.temple.model.Temple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TempleRepository  {
    Page<Temple> findTemplesByRegion(String region, Pageable pageable);
    Optional<Temple> findById(UUID templeId);
    Temple save(Temple temple);
    void delete(UUID templeId);
}
