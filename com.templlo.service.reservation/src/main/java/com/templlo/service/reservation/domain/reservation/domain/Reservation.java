package com.templlo.service.reservation.domain.reservation.domain;

import com.templlo.service.reservation.global.common.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.templlo.service.reservation.global.GlobalConst.PROGRAM_DATE_FORMAT;


@Builder
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name="reservation")
@SQLRestriction("is_deleted = false")
public class Reservation extends BaseEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name="reservation_id", updatable = false, nullable = false)
    private UUID reservationId;

    @Column(name = "program_id", nullable = false)
    private UUID programId;

    @Column(name = "program_date", nullable = false)
    private LocalDate programDate;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservationStatus status;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "phone_number", nullable = false, length = 50)
    private String phoneNumber;

    @Column(name = "reservation_gender_type", nullable = false, length = 20)
    private ReservationGenderType gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false, length = 20)
    private PaymentType paymentType;

    @Column(name = "is_coupon_used", nullable = false)
    private boolean isCouponUsed;

    @Column(name = "coupon_id")
    private UUID couponId;

    public void updateStatusFailed() {
        this.status = ReservationStatus.FAILED;
    }

    public void updateStatusCompleted() {
        this.status = ReservationStatus.COMPLETED;
    }

    public void updateStatusProcessingCancel() {
        this.status = ReservationStatus.PROCESSING_CANCEL;
    }

    public void updateStatusProcessingReject() {
        this.status = ReservationStatus.PROCESSING_REJECT;
    }

    public void updateStatusCanceledOrRejected() {
        switch (this.status) {
            case PROCESSING_CANCEL -> this.updateStatusCanceled();
            case PROCESSING_REJECT -> this.updateStatusRejected();
        }
    }

    public void updateStatusCanceledOrRejectedFailed() {
        switch (this.status) {
            case PROCESSING_CANCEL -> this.updateStatusCancelFailed();
            case PROCESSING_REJECT -> this.updateStatusRejectFailed();
        }
    }

    private void updateStatusCanceled() {
        this.status = ReservationStatus.CANCELED;
    }

    private void updateStatusRejected() {
        this.status = ReservationStatus.REJECTED;
    }

    private void updateStatusCancelFailed() {
        this.status = ReservationStatus.CANCEL_FAILED;
    }

    private void updateStatusRejectFailed() {
        this.status = ReservationStatus.REJECT_FAILED;
    }


    public String getProgramDateFormatted() {
        return DateTimeFormatter.ofPattern(PROGRAM_DATE_FORMAT).format(this.getProgramDate());
    }
}
