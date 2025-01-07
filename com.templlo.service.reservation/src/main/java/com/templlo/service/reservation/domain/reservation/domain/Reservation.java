package com.templlo.service.reservation.domain.reservation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.UUID;


@Builder
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name="reservation")
@SQLRestriction("is_delete = false")
public class Reservation {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name="reservation_id", updatable = false, nullable = false)
    private UUID reservationId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservationStatus status;

    @Column(name = "reservation_date_time", nullable = false)
    private ZonedDateTime reservationDateTime;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "phone_number", nullable = false, length = 50)
    private String phoneNumber;

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
}
