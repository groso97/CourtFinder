package com.project.courtfinder.dto;

import com.project.courtfinder.enums.ReservationStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ReservationDto {
    private Long id;
    private Long userId;
    private String userEmail;
    private Long courtId;
    private String courtName;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate reservationDate;
    private ReservationStatus reservationStatus;
    private BigDecimal price;
    private LocalDateTime createdAt;
}
