package com.project.courtfinder.request;

import com.project.courtfinder.enums.ReservationStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateReservationRequest {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private ReservationStatus reservationStatus;
    private BigDecimal price;
    private Long userId;
    private Long courtId;
}
