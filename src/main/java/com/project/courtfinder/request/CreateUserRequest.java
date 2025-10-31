package com.project.courtfinder.request;

import com.project.courtfinder.enums.UserRole;
import lombok.Data;


@Data
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRole userRole;
}
