package com.project.courtfinder.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class UpdateCourtRequest {
    private String name;
    private String location;
    private String description;
    private BigDecimal pricePerHour;
    private LocalTime availableFrom;
    private LocalTime availableTo;
}
