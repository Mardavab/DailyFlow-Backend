package com.example.dailyflow.backend.backend.models.dto.mapper;

import com.example.dailyflow.backend.backend.models.dto.UserDto;
import com.example.dailyflow.backend.backend.models.entities.User;

public class DtoMapperUser {

    private static DtoMapperUser mapper;

    private User user;

    private DtoMapperUser() {}

    public static DtoMapperUser builder() {
        mapper = new DtoMapperUser();
        return mapper;
    }

    public DtoMapperUser setUser(User user) {
        this.user = user;
        return mapper;
    }

    public UserDto build() {
        if (user == null) {
            throw new RuntimeException("Debe pasar la entidad");
        }
        // Toma el primer rol, si hay (ajusta si quieres la lista)
        String role = null;
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            role = user.getRoles().get(0).getName();
        }
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getDate(),
                role
        );
    }
}
