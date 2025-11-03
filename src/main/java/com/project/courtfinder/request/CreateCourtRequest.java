package com.project.courtfinder.request;

import com.project.courtfinder.enums.CourtType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class CreateCourtRequest {
    private String name;
    private CourtType courtType;
    private String location;
    private String description;
    private BigDecimal pricePerHour;
    private LocalTime availableFrom;
    private LocalTime availableTo;
    private Long ownerId;
}
