package com.moong_bee.blog.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.moong_bee.blog.config.jwt.TokenProvider;
import com.moong_bee.blog.domain.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findByID(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
