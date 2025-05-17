package com.example.dailyflow.backend.backend.services;

import java.util.List;
import java.util.Optional;

import com.example.dailyflow.backend.backend.models.dto.UserRegisterDTO;
import com.example.dailyflow.backend.backend.models.entities.User;

public interface UserService {

    List<User> findAllUsers();

    Optional<User> findUserById(Long id);

    User saveUser(UserRegisterDTO dto);

    Optional<User> updateUser(UserRegisterDTO dto, Long id);

    void removeUser(Long id);

}
