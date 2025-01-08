package com.templlo.service.reservation.domain.reservation.repository;

import com.templlo.service.reservation.domain.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
}
