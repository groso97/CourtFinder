package com.project.courtfinder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.courtfinder.enums.CourtType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courts")
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private CourtType courtType;
    private String location;
    private String description;
    private BigDecimal pricePerHour;
    private LocalTime availableFrom;
    private LocalTime availableTo;
    private LocalDateTime createdAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Court(String name, CourtType courtType, String location, String description, BigDecimal pricePerHour, LocalTime availableFrom, LocalTime availableTo, User owner, List<Reservation> reservations) {
        this.name = name;
        this.courtType = courtType;
        this.location = location;
        this.description = description;
        this.pricePerHour = pricePerHour;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
        this.owner = owner;
        this.reservations = reservations;
    }
}
