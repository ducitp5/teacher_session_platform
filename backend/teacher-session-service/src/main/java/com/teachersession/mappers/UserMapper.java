package com.teachersession.mappers;

import com.teachersession.dto.UserDto;
import com.teachersession.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User entity) {
        if (entity == null) {
            return null;
        }
        return UserDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .role(entity.getRole())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
