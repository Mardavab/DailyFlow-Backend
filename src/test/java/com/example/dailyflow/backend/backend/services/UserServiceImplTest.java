package com.example.dailyflow.backend.backend.services;

import com.example.dailyflow.backend.backend.models.dto.UserRegisterDTO;
import com.example.dailyflow.backend.backend.models.entities.Role;
import com.example.dailyflow.backend.backend.models.entities.User;
import com.example.dailyflow.backend.backend.repositories.RoleRepository;
import com.example.dailyflow.backend.backend.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegisterDTO dto;
    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dto = new UserRegisterDTO();
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setEmail("test@correo.com");
        dto.setRoles(Set.of("ROLE_USER"));

        role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
    }

    @Test
    void testSaveUser() {
        when(passwordEncoder.encode(any())).thenReturn("encryptedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.saveUser(dto);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("test@correo.com", savedUser.getEmail());
        assertEquals("encryptedPassword", savedUser.getPassword());
        assertEquals(1, savedUser.getRoles().size());
    }

    @Test
    void testUpdateUserFound() {
        User user = new User();
        user.setId(1L);
        user.setUsername("olduser");
        user.setEmail("old@correo.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<User> updated = userService.updateUser(dto, 1L);

        assertTrue(updated.isPresent());
        assertEquals("testuser", updated.get().getUsername());
        assertEquals("test@correo.com", updated.get().getEmail());
        assertEquals(1, updated.get().getRoles().size());
    }

    @Test
    void testUpdateUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> updated = userService.updateUser(dto, 1L);

        assertFalse(updated.isPresent());
    }

    @Test
    void testRemoveUser() {
        doNothing().when(userRepository).deleteById(1L);
        userService.removeUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindUserById() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testFindAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(userService.findAllUsers().isEmpty());
    }
}
