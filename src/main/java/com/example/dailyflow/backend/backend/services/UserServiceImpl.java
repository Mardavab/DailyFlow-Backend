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

        // Usa los roles que vienen en el usuario (del frontend)
        List<Role> roles = new ArrayList<>();
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            // Cargar desde la base los roles por id para evitar detached entities
            for (Role role : user.getRoles()) {
                Role managedRole = RoleRepository.findById(role.getId())
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
                roles.add(managedRole);
            }
        } else {
            // Si no viene ninguno, pon por defecto ROLE_USER (opcional)
            Optional<Role> o = RoleRepository.findByName("ROLE_USER");
            if (o.isPresent()) {
                roles.add(o.get());
            }
        }
        user.setRoles(roles);

        return DtoMapperUser.builder().setUser(repository.save(user)).build();
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

            // Aquí agregas la lógica para actualizar roles
            List<Role> roles = new ArrayList<>();
            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                for (Role role : user.getRoles()) {
                    Role managedRole = RoleRepository.findById(role.getId())
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
                    roles.add(managedRole);
                }
                userDb.setRoles(roles);
            }
            // Si no envías roles en el update puedes decidir si los mantienes igual, los dejas vacíos, o pones uno por defecto

            userOptional = repository.save(userDb);
        }
        return Optional.ofNullable(DtoMapperUser.builder().setUser(userOptional).build());
    }
    @Override
    @Transactional
    public void removeUser(Long id) {
        repository.deleteById(id);
    }

}
