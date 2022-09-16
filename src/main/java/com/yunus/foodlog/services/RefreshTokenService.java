package com.yunus.foodlog.services;

import com.yunus.foodlog.entities.RefreshToken;
import com.yunus.foodlog.entities.User;
import com.yunus.foodlog.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${foodlog.refresh.token.expires.in}")
    private Long expireSeconds;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String createRefreshToken(User user) {
        RefreshToken token = refreshTokenRepository.findByUserId(user.getId());
        if(token == null) {
            token = new RefreshToken();
            token.setUser(user);
        }
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Date.from(Instant.now().plusSeconds(expireSeconds)));
        refreshTokenRepository.save(token);
        return token.getToken();
    }

    public boolean isRefreshExpired(RefreshToken token) {
        return token.getExpiryDate().before(new Date());
    }

    public RefreshToken getByUser(Long userId) {
        return refreshTokenRepository.findByUserId(userId);
    }

    /*
    Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5IiwiaWF0IjoxNjYzMzI2MDE3LCJleHAiOjE2NjMzMjYxMzd9.kCZaHSwjBZDvN2WUrO3Bcx2SrqSYOMk3HjYhEl5AT5A

    */
}
