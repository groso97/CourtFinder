package com.project.courtfinder.controller;

import com.project.courtfinder.dto.ReservationDto;
import com.project.courtfinder.exceptions.AlreadyExistsException;
import com.project.courtfinder.exceptions.ResourceNotFoundException;
import com.project.courtfinder.model.Reservation;
import com.project.courtfinder.request.CreateReservationRequest;
import com.project.courtfinder.request.UpdateReservationRequest;
import com.project.courtfinder.response.ApiResponse;
import com.project.courtfinder.services.reservation.IReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/reservations")
public class ReservationController {
    private final IReservationService iReservationService;

    @GetMapping("/{reservationId}/reservation")
    public ResponseEntity<ApiResponse> getReservationById(@PathVariable Long reservationId){
        try {
            Reservation reservation = iReservationService.getReservationById(reservationId);
            ReservationDto reservationDto = iReservationService.convertReservationToDto(reservation);
            return ResponseEntity.ok(new ApiResponse("Success", reservationDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getReservations(){
        List<Reservation> reservations = iReservationService.getAllReservations();
        return ResponseEntity.ok(new ApiResponse("Success", reservations));
    }

    @PatchMapping("/reservation/{reservationId}/cancel")
    public ResponseEntity<ApiResponse> cancelReservation(@PathVariable Long reservationId){
        try {
            Reservation cancelled = iReservationService.cancelReservation(reservationId);
            return ResponseEntity.ok(new ApiResponse("Reservation cancelled successfully!", cancelled));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/create-reservation")
    public ResponseEntity<ApiResponse> createReservation(@RequestBody CreateReservationRequest request){
        try {
            Reservation reservation = iReservationService.createReservation(request);
            ReservationDto reservationDto = iReservationService.convertReservationToDto(reservation);
            return ResponseEntity.ok(new ApiResponse("Reservation created successfully!", reservationDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update/reservation/{reservationId}")
    public ResponseEntity<ApiResponse> updateCourt(@RequestBody UpdateReservationRequest request, @PathVariable Long reservationId){
        try {
            Reservation reservation = iReservationService.updateReservation(request, reservationId);
            ReservationDto reservationDto = iReservationService.convertReservationToDto(reservation);
            return ResponseEntity.ok(new ApiResponse("Reservation updated successfully!", reservationDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
