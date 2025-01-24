package com.templlo.service.program.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.templlo.service.program.auditor.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SQLRestriction(value = "is_deleted = false")
public class TempleStayDailyInfo extends BaseEntity {
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
    @JsonBackReference
    private Program program;

    public static TempleStayDailyInfo create(ProgramStatus programStatus, LocalDate programDate, Integer availableCapacity, Program program) {
        return TempleStayDailyInfo.builder()
                .program(program)
                .status(programStatus)
                .programDate(programDate)
                .availableCapacity(availableCapacity)
                .build();
    }

    public void reduceAvailableCapacity(Integer amount) {
        this.availableCapacity -= amount;

        if (this.availableCapacity == 0) {
            this.status = ProgramStatus.CLOSED;
        }
    }

    public void update(ProgramStatus status) {
        this.status = status;
    }

    public void increaseAvailableCapacity(Integer amount) {
        this.availableCapacity += amount;
        if (this.availableCapacity != 0) {
            this.status = ProgramStatus.ACTIVE;
        }
    }
}
