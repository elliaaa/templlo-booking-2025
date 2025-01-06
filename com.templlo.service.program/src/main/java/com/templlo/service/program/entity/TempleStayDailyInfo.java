package com.templlo.service.program.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TempleStayDailyInfo {
    @Id
    @UuidGenerator
    @Column(name = "temple_stay_daily_info")
    private UUID id;

    @Column(nullable = false)
    private LocalDate programDate;

    @Column(nullable = false)
    private Integer availableCapacity;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ProgramStatus status;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    public static TempleStayDailyInfo create(LocalDate programDate, Integer availableCapacity, Program program) {
        return TempleStayDailyInfo.builder()
                .program(program)
                .programDate(programDate)
                .availableCapacity(availableCapacity)
                .status(ProgramStatus.INACTIVE)
                .build();
    }
}
