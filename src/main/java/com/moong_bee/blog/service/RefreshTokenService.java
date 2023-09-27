package com.moong_bee.blog.service;

import org.springframework.stereotype.Service;

import com.moong_bee.blog.domain.RefreshToken;
import com.moong_bee.blog.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
