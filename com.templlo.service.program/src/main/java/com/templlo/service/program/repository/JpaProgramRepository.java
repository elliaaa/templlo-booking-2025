package com.templlo.service.program.repository;

import com.templlo.service.program.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaProgramRepository extends JpaRepository<Program, UUID> {
}
