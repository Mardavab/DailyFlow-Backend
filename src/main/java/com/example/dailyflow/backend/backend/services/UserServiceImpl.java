package com.example.dailyflow.backend.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dailyflow.backend.backend.models.entities.User;
import com.example.dailyflow.backend.backend.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return (List<User>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public void removeUser(Long id) {
        repository.deleteById(id);

    }

    @Override
    @Transactional
    public User saveUser(User user) {
        return repository.save(user);
    }

    @Override
    public Optional<User> updateUser(User user, Long id){
        Optional<User> o = findUserById(id);
        if (o.isPresent()) {
            User userDb = o.orElseThrow();
            userDb.setUsername((user.getUsername()));
            userDb.setEmail(user.getEmail());
            return Optional.of(this.saveUser(userDb));
        }
        return Optional.empty();
    }

}
