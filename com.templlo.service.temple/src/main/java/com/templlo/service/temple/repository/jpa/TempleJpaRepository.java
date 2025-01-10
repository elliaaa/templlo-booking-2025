package com.templlo.service.temple.repository.jpa;

import com.templlo.service.temple.model.Temple;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TempleJpaRepository extends JpaRepository<Temple, UUID> {
}
