package com.formalhaut.fmh.service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import com.formalhaut.fmh.entity.RefreshTokenEntity;
import com.formalhaut.fmh.repository.RefreshTokenRepository;
import com.formalhaut.fmh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class RefreshTokenService {
    @Value("${fmh.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    public RefreshTokenEntity findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    public RefreshTokenEntity createRefreshToken(Integer userId) {
        Integer user_id = userRepository.findByUserId(userId).getId();
        Date expiry_date = new Date(System.currentTimeMillis() + refreshTokenDurationMs);
        String token = UUID.randomUUID().toString();
        RefreshTokenEntity refreshToken = null;
        refreshToken.setUser_id(user_id);
        refreshToken.setExpiry_date(expiry_date);
        refreshToken.setToken(token);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) throws Exception {
        if (!token.getExpiry_date().before(new Date())) {
            refreshTokenRepository.delete(String.valueOf(token));
            throw new Exception("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
    @Transactional
    public void deleteByUserId(Integer userId) {
        refreshTokenRepository.deleteByUser(userRepository.findByUserId(userId).getId());
        return;
    }
}