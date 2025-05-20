package com.example.dailyflow.backend.backend.services;

import java.util.List;
import java.util.Optional;

import com.example.dailyflow.backend.backend.models.dto.UserDto;
import com.example.dailyflow.backend.backend.models.entities.User;
import com.example.dailyflow.backend.backend.models.request.UserRequest;

public interface UserService {

    List<UserDto> findAllUsers();

    Optional<UserDto> findUserById(Long id);

    UserDto saveUser(User user);

    Optional<UserDto> updateUser(UserRequest user, Long id);

    void removeUser(Long id);

}
