package com.moong_bee.blog.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.moong_bee.blog.domain.User;
import com.moong_bee.blog.dto.AddUserRequest;
import com.moong_bee.blog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }

    public User findByID(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    
    }
}
