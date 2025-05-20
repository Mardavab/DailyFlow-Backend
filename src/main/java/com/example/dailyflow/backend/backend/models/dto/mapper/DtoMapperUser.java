package com.example.dailyflow.backend.backend.models.dto.mapper;

import com.example.dailyflow.backend.backend.models.dto.UserDto;
import com.example.dailyflow.backend.backend.models.entities.User;

public class DtoMapperUser {

    private static DtoMapperUser mapper;

    private User user;

    private DtoMapperUser() {

    }

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
        return new UserDto(this.user.getId(), user.getEmail(), user.getUsername());

    }

}
