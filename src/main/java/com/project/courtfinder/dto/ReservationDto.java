package com.project.courtfinder.dto;

import com.project.courtfinder.enums.ReservationStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReservationDto {
    private Long id;
    private Long userId;
    private String userEmail;
    private Long courtId;
    private String courtName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private ReservationStatus reservationStatus;
    private BigDecimal price;
    private LocalDateTime createdAt;
}
