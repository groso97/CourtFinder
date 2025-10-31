package com.project.courtfinder.request;

import com.project.courtfinder.enums.UserRole;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private UserRole userRole;
}
