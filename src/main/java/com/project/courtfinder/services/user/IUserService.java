package com.project.courtfinder.services.user;

import com.project.courtfinder.dto.UserDto;
import com.project.courtfinder.model.User;
import com.project.courtfinder.request.CreateUserRequest;
import com.project.courtfinder.request.UpdateUserRequest;

import java.util.List;

public interface IUserService {
    User createUser(CreateUserRequest user);
    User getUserById(Long id);
    List<User> getAllUsers();
    void deleteUserById(Long id);
    User updateUser(UpdateUserRequest user, Long userId);

    UserDto convertUserToDto(User user);
}
