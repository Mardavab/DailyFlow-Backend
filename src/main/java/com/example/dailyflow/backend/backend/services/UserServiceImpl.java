package com.example.dailyflow.backend.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dailyflow.backend.backend.models.dto.UserDto;
import com.example.dailyflow.backend.backend.models.dto.mapper.DtoMapperUser;
import com.example.dailyflow.backend.backend.models.entities.Role;
import com.example.dailyflow.backend.backend.models.entities.User;
import com.example.dailyflow.backend.backend.models.request.UserRequest;
import com.example.dailyflow.backend.backend.repositories.RoleRepository;
import com.example.dailyflow.backend.backend.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository RoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {
        List<User> users = (List<User>) repository.findAll();

        return users.stream().map(u -> DtoMapperUser.builder().setUser(u).build()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findUserById(Long id) {
        Optional<User> o = repository.findById(id);
        if (o.isPresent()) {
            return Optional.of(
                DtoMapperUser.builder().setUser(o.orElseThrow()).build()
            );   
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public UserDto saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<Role> o = RoleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        if (o.isPresent()) {
            roles.add(o.orElseThrow());

        }
        user.setRoles(roles);
        return  DtoMapperUser.builder().setUser(repository.save(user)).build() ;
    }

    @Override
    @Transactional
    public Optional<UserDto> updateUser(UserRequest user, Long id) {
        Optional<User> o = repository.findById(id);
        User userOptional = null;
        if (o.isPresent()) {
            User userDb = o.orElseThrow();
            userDb.setUsername(user.getUsername());
            userDb.setEmail(user.getEmail());
            userOptional = repository.save(userDb);
        }
        return Optional.ofNullable(DtoMapperUser.builder().setUser(repository.save(userOptional)).build());
    }

    @Override
    @Transactional
    public void removeUser(Long id) {
        repository.deleteById(id);
    }

}
