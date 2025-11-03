package com.project.courtfinder.controller;

import com.project.courtfinder.dto.UserDto;
import com.project.courtfinder.exceptions.AlreadyExistsException;
import com.project.courtfinder.exceptions.ResourceNotFoundException;
import com.project.courtfinder.model.Court;
import com.project.courtfinder.model.User;
import com.project.courtfinder.request.CreateUserRequest;
import com.project.courtfinder.request.UpdateUserRequest;
import com.project.courtfinder.response.ApiResponse;
import com.project.courtfinder.services.court.ICourtService;
import com.project.courtfinder.services.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService iUserService;
    private final ICourtService iCourtService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        try {
            User user = iUserService.getUserById(userId);
            UserDto userDto = iUserService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getUsers(){
        List<User> users = iUserService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse("Success", users));
    }

    @DeleteMapping("/delete/user/{userId}")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Long userId){
        try {
            iUserService.deleteUserById(userId);
            return ResponseEntity.ok(new ApiResponse("User deleted successfully!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request){
        try {
            User user = iUserService.createUser(request);
            UserDto userDto = iUserService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("User created successfully!", userDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update/user/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request, @PathVariable Long userId) {
        try {
            User user = iUserService.updateUser(request, userId);
            UserDto userDto = iUserService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("User updated successfully!", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{userId}/courts")
    public ResponseEntity<ApiResponse> getUserCourts(@PathVariable Long userId){
        try {
            List<Court> courts = iCourtService.getCourtsByOwnerId(userId);
            return ResponseEntity.ok(new ApiResponse("Success", courts));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
