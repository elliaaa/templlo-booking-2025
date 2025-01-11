package com.templlo.service.program.entity;

import com.templlo.service.program.auditor.BaseEntity;
import com.templlo.service.program.kafka.message.reservation.Gender;
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

    public static BlindDateInfo create(Program program, ProgramStatus programStatus, LocalDate programDate, Integer maleCapacity, Integer femaleCapacity) {
        return BlindDateInfo.builder()
                .program(program)
                .status(programStatus)
                .genderStatus(ProgramGenderStatus.OPEN)
                .programDate(programDate)
                .maleCapacity(maleCapacity)
                .femaleCapacity(femaleCapacity)
                .availableMaleCapacity(maleCapacity)
                .availableFemaleCapacity(femaleCapacity)
                .build();
    }

    public void reduceAvailableCapacity(Gender gender) {
        // 남성
        if (gender == Gender.MALE) {
            // 정원 감소
            this.availableMaleCapacity -= 1;
            // 남성 가능 정원이 0 이라면
            if (availableMaleCapacity == 0) {
                // 남성 가능 정원이 0이 되었는데 이미 여성 정원도 마감이라면, 해당 스케쥴 마감으로 변경
                if (this.genderStatus == ProgramGenderStatus.FEMALE_CLOSED) {
                    this.status = ProgramStatus.CLOSED;
                } else {
                    this.genderStatus = ProgramGenderStatus.MALE_CLOSED;
                }
            }
        }
        // 여성
        if (gender == Gender.FEMALE) {

            this.availableFemaleCapacity -= 1;

            if (availableFemaleCapacity == 0) {
                if (this.genderStatus == ProgramGenderStatus.MALE_CLOSED) {
                    this.status = ProgramStatus.CLOSED;
                } else {
                    this.genderStatus = ProgramGenderStatus.FEMALE_CLOSED;
                }
            }
        }
    }
}
