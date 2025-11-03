package com.project.courtfinder.services.reservation;

import com.project.courtfinder.dto.ReservationDto;
import com.project.courtfinder.enums.ReservationStatus;
import com.project.courtfinder.exceptions.AlreadyExistsException;
import com.project.courtfinder.exceptions.ResourceNotFoundException;
import com.project.courtfinder.model.Court;
import com.project.courtfinder.model.Reservation;
import com.project.courtfinder.model.User;
import com.project.courtfinder.repository.CourtRepository;
import com.project.courtfinder.repository.ReservationRepository;
import com.project.courtfinder.repository.UserRepository;
import com.project.courtfinder.request.CreateReservationRequest;
import com.project.courtfinder.request.UpdateReservationRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final CourtRepository courtRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    public Reservation createReservation(CreateReservationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new ResourceNotFoundException("User with id " + request.getUserId() + " not found!"));
        Court court = courtRepository.findById(request.getCourtId())
                .orElseThrow(()-> new ResourceNotFoundException("Court with id " + request.getCourtId() + " not found!"));

        boolean courtTaken = reservationRepository.existsByCourtAndTimeRange(
                court.getId(), request.getReservationDate(), request.getStartTime(), request.getEndTime()
        );

        if(courtTaken){
            throw new AlreadyExistsException("Court with id: " + court.getId() + " is already reserved at that time!");
        }

        boolean userBusy = reservationRepository.existsByUserAndTimeRange(
                user.getId(), request.getReservationDate(), request.getStartTime(), request.getEndTime()
        );

        if(userBusy){
            throw new AlreadyExistsException("User with id: " + user.getId() + " already has a reservation at that time!");
        }

        if(request.getStartTime().isBefore(court.getAvailableFrom())
        || request.getEndTime().isAfter(court.getAvailableTo())){
            throw new AlreadyExistsException("Time is outside of court working hours!");
        }

        long hours = Duration.between(request.getStartTime(), request.getEndTime()).toHours();
        if (hours <= 0) {
            hours += 24;
        }
        BigDecimal price = court.getPricePerHour().multiply(BigDecimal.valueOf(hours));

       Reservation reservation = new Reservation(
               user,
               court,
               request.getStartTime(),
               request.getEndTime(),
               request.getReservationDate(),
               price,
               ReservationStatus.PENDING
       );

       return reservationRepository.save(reservation);
    }

    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Reservation with id " + id + " not found!"));
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation updateReservation(UpdateReservationRequest updateReservationRequest, Long reservationId) {
        Reservation updatedReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation with id " + reservationId + " not found!"));

        updatedReservation.setStartTime(updateReservationRequest.getStartTime());
        updatedReservation.setEndTime(updateReservationRequest.getEndTime());
        updatedReservation.setReservationDate(updateReservationRequest.getReservationDate());
        updatedReservation.setReservationStatus(updateReservationRequest.getReservationStatus());
        updatedReservation.setPrice(updateReservationRequest.getPrice());
        return reservationRepository.save(updatedReservation);
    }

    @Override
    public ReservationDto convertReservationToDto(Reservation reservation) {
        return modelMapper.map(reservation, ReservationDto.class);
    }

    @Override
    public Reservation cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(()-> new ResourceNotFoundException("Reservation with id " + reservationId + " not found!"));

        LocalDateTime reservationDateTime = LocalDateTime.of(reservation.getReservationDate(), reservation.getStartTime());
        if (Duration.between(LocalDateTime.now(), reservationDateTime).toHours() < 8) {
            throw new IllegalStateException("Reservation can only be cancelled at least 8 hours before start time!");
        }
        reservation.setReservationStatus(ReservationStatus.CANCELLED);
        return reservationRepository.save(reservation);
    }
}
