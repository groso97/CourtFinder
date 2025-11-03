package com.project.courtfinder.repository;

import com.project.courtfinder.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END
        FROM Reservation r
        WHERE r.court.id = :courtId
          AND r.reservationDate = :reservationDate
          AND r.reservationStatus IN ('PENDING', 'CONFIRMED')
          AND (r.startTime < :endTime AND r.endTime > :startTime)
    """)
    boolean existsByCourtAndTimeRange(
            @Param("courtId") Long courtId,
            @Param("reservationDate") LocalDate reservationDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END
        FROM Reservation r
        WHERE r.user.id = :userId
          AND r.reservationDate = :reservationDate
          AND r.reservationStatus IN ('PENDING', 'CONFIRMED')
          AND (r.startTime < :endTime AND r.endTime > :startTime)
    """)
    boolean existsByUserAndTimeRange(
            @Param("userId") Long userId,
            @Param("reservationDate") LocalDate reservationDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
