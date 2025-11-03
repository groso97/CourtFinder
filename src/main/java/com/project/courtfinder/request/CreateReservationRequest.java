package com.project.courtfinder.request;

import com.project.courtfinder.enums.ReservationStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateReservationRequest {
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate reservationDate;
    private ReservationStatus reservationStatus;
    private BigDecimal price;
    private Long userId;
    private Long courtId;
}
