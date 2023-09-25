package com.moong_bee.blog.config.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.moong_bee.blog.domain.User;
import com.moong_bee.blog.repository.UserRepository;

import io.jsonwebtoken.Jwts;

@SpringBootTest
class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProperties jwtProperties;

    @DisplayName("generateToken(): 유저 정보, 만료 기간을 전달해 토큰 생성")
    @Test
    void generateToken() {
        User testUser = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        String token = tokenProvider.generateToken(testUser, Duration.ofDays(4));

        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    @DisplayName("validToken(): 토큰 만료 검증")
    @Test
    void validToken_invalid() {
        String token = JwtFactory.builder()
                .expriration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis())).build()
                .createToken(jwtProperties);

        boolean result = tokenProvider.validToken(token);

        assertThat(result).isFalse();
    }

    @DisplayName("validToken(): 토큰 유효 검증")
    @Test
    void validToken_valid() {
        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);

        boolean result = tokenProvider.validToken(token);

        assertThat(result).isTrue();
    }

    @DisplayName("getAuthentication(): 토큰 정보 확인")
    @Test
    void getAuthentication() {
        String userEmail = "user@gmail.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        Authentication authentication = tokenProvider.getAuthentication(token);

        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    @DisplayName("getUserId(): 토큰 유저 정보 확인")
    @Test
    void getUserId() {
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        Long userIdByToken = tokenProvider.getUserId(token);

        assertThat(userIdByToken).isEqualTo(userId);
    }
}
