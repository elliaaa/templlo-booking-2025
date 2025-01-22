package com.templlo.service.reservation.domain.reservation.repository;

import com.querydsl.core.types.Predicate;
import com.templlo.service.reservation.domain.reservation.controller.model.response.ReservationListRes;
import com.templlo.service.reservation.domain.reservation.domain.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.web.PagedModel;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID>, ReservationRepositoryCustom, QuerydslPredicateExecutor<Reservation> {
    Page<Reservation> findAllByUserId(UUID userId, Pageable pageable);

    Page<Reservation> findAllByProgramIdIn(List<UUID> programIds, Pageable pageable);

    default PagedModel<ReservationListRes> findAllByUserIdOfPagedModel(UUID userId, Pageable pageable) {
        Page<Reservation> pageEntity = findAllByUserId(userId, pageable);
        return toReservationListResPagedModel(pageEntity);
    }

    default PagedModel<ReservationListRes> findAllByByProgramIdOfPagedModel(List<UUID> programIds, Pageable pageable) {
        Page<Reservation> pageEntity = findAllByProgramIdIn(programIds, pageable);
        return toReservationListResPagedModel(pageEntity);
    }

    default PagedModel<ReservationListRes> findAllInPagedModel(Predicate predicate, Pageable pageable) {
        Page<Reservation> pageReservation = findAll(predicate, pageable);
        return toReservationListResPagedModel(pageReservation);
    }

    private static PagedModel<ReservationListRes> toReservationListResPagedModel(Page<Reservation> pageEntity) {
        Page<ReservationListRes> pageDto = pageEntity.map(ReservationListRes::from);
        return new PagedModel<>(pageDto);
    }


}
