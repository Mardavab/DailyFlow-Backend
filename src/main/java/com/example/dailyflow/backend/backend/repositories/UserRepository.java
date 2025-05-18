package com.example.dailyflow.backend.backend.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.dailyflow.backend.backend.models.entities.User;

public interface UserRepository extends CrudRepository<User,Long>{
Optional<User>findByUsername(String username);
}

