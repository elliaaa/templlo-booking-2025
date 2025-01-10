package com.templlo.service.temple.repository.jpa;

import com.templlo.service.temple.model.Temple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TempleCustomRepository {
    Page<Temple> findTemplesByRegion(String region, Pageable pageable);
}
