package com.teachersession.services;

import com.teachersession.dto.UserDto;
import com.teachersession.entities.User;
import com.teachersession.entities.enums.Role;
import com.teachersession.mappers.UserMapper;
import com.teachersession.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto register(String email, String password, String firstName, String lastName, Role role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        
        User user = User.builder()
                .email(email)
                .password(password) // Note: In a real app we'd hash this
                .firstName(firstName)
                .lastName(lastName)
                .role(role)
                .build();
                
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    public Optional<UserDto> login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password)) // In real app use passwordEncoder
                .map(userMapper::toDto);
    }
}
