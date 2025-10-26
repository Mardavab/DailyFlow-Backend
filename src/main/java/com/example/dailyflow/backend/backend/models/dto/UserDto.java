package com.example.dailyflow.backend.backend.models.dto;

import java.time.LocalDate;

public class UserDto {

    private Long id;
    private String username;
    private String email;
    private LocalDate date;
    private String role; 

    public UserDto() {}

    public UserDto(Long id, String username, String email, LocalDate date, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.date = date;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
