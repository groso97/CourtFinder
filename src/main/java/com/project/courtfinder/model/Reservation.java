package com.project.courtfinder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.courtfinder.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate reservationDate;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
    private BigDecimal price;
    private LocalDateTime createdAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "court_id")
    private Court court;

    public Reservation(User user, Court court, LocalTime startTime, LocalTime endTime, LocalDate reservationDate, BigDecimal price, ReservationStatus reservationStatus) {
        this.user = user;
        this.court = court;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservationDate = reservationDate;
        this.price = price;
        this.reservationStatus = reservationStatus;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
