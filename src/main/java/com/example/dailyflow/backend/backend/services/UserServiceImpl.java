package com.example.dailyflow.backend.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dailyflow.backend.backend.models.dto.UserRegisterDTO;
import com.example.dailyflow.backend.backend.models.entities.Role;
import com.example.dailyflow.backend.backend.models.entities.User;
import com.example.dailyflow.backend.backend.repositories.RoleRepository;
import com.example.dailyflow.backend.backend.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public void removeUser(Long id) {
        userRepository.deleteById(id);

    }

    @Override
    @Transactional
    public User saveUser(UserRegisterDTO dto) {
        User user = new User();
        String passwordBc = passwordEncoder.encode(user.getPassword());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordBc);

        Set<Role> roleEntities = dto.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roleEntities);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> updateUser(UserRegisterDTO dto, Long id) {
        Optional<User> o = findUserById(id);
        if (o.isPresent()) {
            User userDb = o.get();
            userDb.setUsername(dto.getUsername());
            userDb.setEmail(dto.getEmail());

            Set<Role> roles = dto.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                    .collect(Collectors.toSet());
            userDb.setRoles(roles);

            return Optional.of(userRepository.save(userDb));
        }
        return Optional.empty();
    }

}
