package com.templlo.service.program.repository;

import com.templlo.service.program.entity.TempleStayDailyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaTempleStayDailyInfoRepository extends JpaRepository<TempleStayDailyInfo, UUID> {
    Optional<TempleStayDailyInfo> findByProgram_IdAndProgramDate(UUID programId, LocalDate programDate);
}
