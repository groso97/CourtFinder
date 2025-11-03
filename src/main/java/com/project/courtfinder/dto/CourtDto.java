package com.project.courtfinder.dto;

import com.project.courtfinder.enums.CourtType;
import com.project.courtfinder.model.Reservation;
import com.project.courtfinder.model.User;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class CourtDto {
    private String name;
    private CourtType courtType;
    private String location;
    private String description;
    private BigDecimal pricePerHour;
    private LocalTime availableFrom;
    private LocalTime availableTo;
    private LocalDateTime createdAt;
    private User owner;
    private List<Reservation> reservations;
}
