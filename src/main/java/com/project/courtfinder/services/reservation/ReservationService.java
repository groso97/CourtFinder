package com.project.courtfinder.services.reservation;

import com.project.courtfinder.dto.ReservationDto;
import com.project.courtfinder.enums.ReservationStatus;
import com.project.courtfinder.exceptions.AlreadyExistsException;
import com.project.courtfinder.exceptions.InvalidReservationTimeException;
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
import java.time.LocalTime;
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
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + request.getUserId() + " not found!"));
        Court court = courtRepository.findById(request.getCourtId())
                .orElseThrow(() -> new ResourceNotFoundException("Court with id " + request.getCourtId() + " not found!"));

        LocalDateTime start = request.getStartDateTime();
        LocalDateTime end = request.getEndDateTime();

        if (start.getMinute() != 0 || end.getMinute() != 0) {
            throw new InvalidReservationTimeException("Reservations must start and end on the hour (full hours only).");
        }

        if (!end.isAfter(start)) {
            throw new InvalidReservationTimeException("End time must be after start time.");
        }

        long hours = Duration.between(start, end).toHours();
        if (hours < 1) throw new InvalidReservationTimeException("Minimum reservation duration is 1 hour.");
        if (hours > 2) throw new InvalidReservationTimeException("Maximum reservation duration is 2 hours.");

        LocalTime courtFrom = court.getAvailableFrom();
        LocalTime courtTo = court.getAvailableTo();

        LocalDateTime courtOpen = LocalDateTime.of(start.toLocalDate(), courtFrom);
        LocalDateTime courtClose = courtTo.equals(LocalTime.MIDNIGHT)
                ? LocalDateTime.of(start.toLocalDate().plusDays(1), LocalTime.MIDNIGHT)
                : LocalDateTime.of(start.toLocalDate(), courtTo);

        if (start.isBefore(courtOpen)) {
            throw new InvalidReservationTimeException("Reservation cannot start before court opens!");
        }

        if (end.isAfter(courtClose)) {
            throw new InvalidReservationTimeException("Reservation cannot end after court closing time!");
        }

        List<Reservation> courtConflicts = reservationRepository.findAllByCourtAndReservationStatusIn(
                        court, List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED))
                .stream()
                .filter(r -> start.isBefore(r.getEndDateTime()) && end.isAfter(r.getStartDateTime()))
                .toList();

        if (!courtConflicts.isEmpty()) {
            throw new AlreadyExistsException("Court is already reserved at that time!");
        }

        List<Reservation> userConflicts = reservationRepository.findAllByUserAndReservationStatusIn(
                        user, List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED))
                .stream()
                .filter(r -> start.isBefore(r.getEndDateTime()) && end.isAfter(r.getStartDateTime()))
                .toList();

        if (!userConflicts.isEmpty()) {
            throw new AlreadyExistsException("You already have a reservation at that time!");
        }

        BigDecimal price = court.getPricePerHour().multiply(BigDecimal.valueOf(hours));

        Reservation reservation = new Reservation(
                user,
                court,
                start,
                end,
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
        Reservation existing = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation with id " + reservationId + " not found!"));

        existing.setStartDateTime(updateReservationRequest.getStartDateTime());
        existing.setEndDateTime(updateReservationRequest.getEndDateTime());
        existing.setReservationStatus(updateReservationRequest.getReservationStatus());
        existing.setPrice(updateReservationRequest.getPrice());
        return reservationRepository.save(existing);
    }

    @Override
    public ReservationDto convertReservationToDto(Reservation reservation) {
        return modelMapper.map(reservation, ReservationDto.class);
    }

    @Override
    public Reservation cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(()-> new ResourceNotFoundException("Reservation with id " + reservationId + " not found!"));

        if (Duration.between(LocalDateTime.now(), reservation.getStartDateTime()).toHours() < 8) {
            throw new IllegalStateException("Reservation can only be cancelled at least 8 hours before start time!");
        }
        reservation.setReservationStatus(ReservationStatus.CANCELLED);
        return reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservationById(Long reservationId) {
        reservationRepository.findById(reservationId)
                .ifPresentOrElse(reservationRepository::delete,
                        ()-> {throw new ResourceNotFoundException("Reservation with id " + reservationId + " not found!");
                        });
    }
}
