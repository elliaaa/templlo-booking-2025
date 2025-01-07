package com.templlo.service.program.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.templlo.service.program.auditor.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Program extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "program_id")
    private UUID id;

    @Column(nullable = false)
    private UUID templeId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer programCapacity;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ProgramType type;

    @Column(nullable = false)
    private Integer programFee;

    @Column(nullable = false)
    private Double programRating;

    @Column(nullable = false)
    private LocalTime programStartAt;

    @Column(nullable = false)
    private LocalDate reservationStartDate;

    @Column(nullable = false)
    private LocalDate reservationEndDate;

    @Column(columnDefinition = "text")
    private String programDays;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
    List<TempleStayDailyInfo> templeStayDailyInfos = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "blind_date_info_id")
    private BlindDateInfo blindDateInfo;

    public static Program create(
            UUID templeId,
            String title,
            String description,
            Integer programCapacity,
            ProgramType type,
            Integer programFee,
            List<String> programDays,
            LocalTime programStartAt,
            LocalDate reservationStartDate,
            LocalDate reservationEndDate
            ) {

        return Program.builder()
                .templeId(templeId)
                .title(title)
                .description(description)
                .programCapacity(programCapacity)
                .type(type)
                .programFee(programFee)
                .programDays(convertProgramDaysToString(programDays))
                .programRating(0.0)
                .programStartAt(programStartAt)
                .reservationStartDate(reservationStartDate)
                .reservationEndDate(reservationEndDate)
                .build();
    }

    public void addTempleStayDailInfo(Program program, LocalDate startDate, LocalDate endDate, List<String> programDays, Integer programCapacity) {

        // 프로그램 요일을 DayOfWeek로 변환 -> LocalDate에서 요일을 가져올 때 getDayOfWeek() 는 DayOfWeek 타입을 반환
        List<DayOfWeek> programDaysOfWeek = programDays.stream()
                .map(day -> DayOfWeek.valueOf(day.toUpperCase()))
                .toList();

        List<TempleStayDailyInfo> templeStayDailyInfos = new ArrayList<>();

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {

            if (programDaysOfWeek.contains(currentDate.getDayOfWeek())) {

                templeStayDailyInfos.add(
                        TempleStayDailyInfo.create(
                                currentDate,
                                programCapacity,
                                program
                        )
                );
            }
            currentDate = currentDate.plusDays(1);
        }

        this.templeStayDailyInfos = templeStayDailyInfos;

    }

    public void addBlindDateInfo(BlindDateInfo blindDateInfo) {
        this.blindDateInfo = blindDateInfo;
    }

    public List<String> convertProgramDaysToList(String programDays) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(programDays, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static String convertProgramDaysToString(List<String> programDaysList) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(programDaysList);
        } catch (Exception e) {
            return "[]";
        }
    }

}
