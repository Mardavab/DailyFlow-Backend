package com.example.dailyflow.backend.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.dailyflow.backend.backend.models.entities.User;

public interface UserRepository extends CrudRepository<User,Long>{
    Optional<User>findByUsername(String username);

    @Query("select u from User u where u.username=?1")
    Optional<User> getUserByUsername(String username);
}

