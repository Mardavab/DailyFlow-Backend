package com.example.dailyflow.backend.backend.controllers;

import com.example.dailyflow.backend.backend.models.dto.UserRegisterDTO;
import com.example.dailyflow.backend.backend.models.entities.User;
import com.example.dailyflow.backend.backend.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("usuario1");
        user.setEmail("usuario1@correo.com");
        user.setPassword("password123");
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.findUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("usuario1"));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        when(userService.findUserById(10L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser() throws Exception {
        when(userService.saveUser(any(UserRegisterDTO.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"usuario1\",\"password\":\"password123\",\"email\":\"usuario1@correo.com\",\"roles\":[\"ROLE_USER\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("usuario1"));
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userService.updateUser(any(UserRegisterDTO.class), eq(1L))).thenReturn(Optional.of(user));

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"usuario1\",\"email\":\"usuario1@correo.com\",\"roles\":[\"ROLE_USER\"]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("usuario1"));
    }

    @Test
    void testDeleteUser() throws Exception {
        when(userService.findUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        when(userService.findUserById(404L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/users/404")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
