package com.project.courtfinder.services.reservation;

import com.project.courtfinder.dto.ReservationDto;
import com.project.courtfinder.model.Reservation;
import com.project.courtfinder.request.CreateReservationRequest;
import com.project.courtfinder.request.UpdateReservationRequest;

import java.util.List;

public interface IReservationService {
    Reservation createReservation(CreateReservationRequest createReservationRequest);
    Reservation getReservationById(Long id);
    List<Reservation> getAllReservations();
    Reservation updateReservation(UpdateReservationRequest updateReservationRequest, Long courtId);
    ReservationDto convertReservationToDto(Reservation reservation);
    Reservation cancelReservation(Long reservationId);
}
