package com.templlo.service.program.entity;

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
public class BlindDateInfo extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "blind_date_info_id")
    private UUID id;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ProgramStatus status;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ProgramGenderStatus genderStatus;

    @Column(nullable = false)
    private LocalDate programDate;

    @Column(nullable = false)
    private Integer maleCapacity;

    @Column(nullable = false)
    private Integer femaleCapacity;

    @Column(nullable = false)
    private Integer availableMaleCapacity;

    @Column(nullable = false)
    private Integer availableFemaleCapacity;

    private LocalDate additionalReservationStartDate;

    private LocalDate additionalReservationEndDate;

    @OneToOne(mappedBy = "blindDateInfo")
    private Program program;

    public static BlindDateInfo create(Program program, LocalDate programDate, Integer maleCapacity, Integer femaleCapacity) {
        return BlindDateInfo.builder()
                .program(program)
                .status(ProgramStatus.INACTIVE)
                .genderStatus(ProgramGenderStatus.OPEN)
                .programDate(programDate)
                .maleCapacity(maleCapacity)
                .femaleCapacity(femaleCapacity)
                .availableMaleCapacity(maleCapacity)
                .availableFemaleCapacity(femaleCapacity)
                .build();
    }

}
