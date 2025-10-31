package com.project.courtfinder.services.user;

import com.project.courtfinder.dto.UserDto;
import com.project.courtfinder.exceptions.AlreadyExistsException;
import com.project.courtfinder.exceptions.ResourceNotFoundException;
import com.project.courtfinder.model.User;
import com.project.courtfinder.repository.UserRepository;
import com.project.courtfinder.request.CreateUserRequest;
import com.project.courtfinder.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    public User createUser(CreateUserRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new AlreadyExistsException("User with email " +request.getEmail() + " already exists!");
        }
        User newUser = new User();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());
        newUser.setUserRole(request.getUserRole());

        return userRepository.save(newUser);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User with id " + id + " not found!"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.findById(id)
                .ifPresentOrElse(userRepository::delete,
                        ()-> {
                            throw new ResourceNotFoundException("User with id " + id + " not found!");
                        });
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long id) {
        User updatedUser = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User with id " + id + " not found!"));
        updatedUser.setFirstName(request.getFirstName());
        updatedUser.setLastName(request.getLastName());
        updatedUser.setUserRole(request.getUserRole());
        return userRepository.save(updatedUser);
    }

    @Override
    public UserDto convertUserToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }
}
