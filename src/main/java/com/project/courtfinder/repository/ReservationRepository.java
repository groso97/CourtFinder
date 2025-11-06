package com.project.courtfinder.repository;

import com.project.courtfinder.enums.ReservationStatus;
import com.project.courtfinder.model.Court;
import com.project.courtfinder.model.Reservation;
import com.project.courtfinder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByCourtAndReservationStatusIn(Court court, List<ReservationStatus> statuses);
    List<Reservation> findAllByUserAndReservationStatusIn(User user, List<ReservationStatus> statuses);
}
