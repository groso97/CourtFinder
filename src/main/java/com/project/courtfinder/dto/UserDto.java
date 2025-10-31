package com.project.courtfinder.dto;

import com.project.courtfinder.enums.UserRole;
import com.project.courtfinder.model.Court;
import com.project.courtfinder.model.Reservation;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRole userRole;
    private boolean confirmed;
    private LocalDateTime createdAt;
    private List<Reservation> reservations;
    private List<Court> courts;
}
