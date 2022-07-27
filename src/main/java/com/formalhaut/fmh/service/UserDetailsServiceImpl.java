package com.formalhaut.fmh.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.formalhaut.fmh.entity.UserEntity;
import com.formalhaut.fmh.model.UserIssueToken;
import com.formalhaut.fmh.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${jwt.accesstoken.expirationtime}")
    private long accessTokenExpTime;

    @Value("${jwt.refreshtoken.expirationtime}")
    private long refreshTokenExpTime;

    @Value("${jwt.accesstoken.secretkey}")
    private String accessTokenSecret;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            UserEntity entity = userRepository.findByUsername(username);
            return new User(entity.getUsername(), PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(entity.getPassword()), new ArrayList<>());
        }catch (Exception e) {
            throw new UsernameNotFoundException("ユーザーが見つかりません");
        }
    }

    @Transactional
    public UserIssueToken issueToken(String username) throws UsernameNotFoundException {
        UserEntity entity = userRepository.findByUsername(username);

        final Instant now = Instant.now();

        final String token = createAccessToken(username);

        return UserIssueToken.builder()
                .accessToken(token)
                .refreshToken(generateRefreshToken(entity))
                .build();
    }

    public String createAccessToken(String username){
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plus(accessTokenExpTime, ChronoUnit.SECONDS)))
                .sign(Algorithm.HMAC512(accessTokenSecret.getBytes()));
    }

    public String generateRefreshToken(UserEntity entity) {
        final String token = RandomStringUtils.randomAlphanumeric(16);;
        entity.setRefreshToken(passwordEncoder.encode(token));
        entity.setRefreshTokenIssuedAt(Instant.now());
        userRepository.updateToken(entity);
        userRepository.updateTokenIssuedAt(entity);
        return token;
    }

    @Transactional(readOnly = true)
    public boolean verifyRefreshToken(String username, String refreshToken) throws UsernameNotFoundException {
        UserEntity entity = userRepository.findByUsername(username);

        if(entity.getRefreshTokenIssuedAt() != null &&
                entity.getRefreshTokenIssuedAt().plus(refreshTokenExpTime, ChronoUnit.SECONDS).isBefore(Instant.now())) {
            logger.info("Refresh token of {} is already expired", username);
            return false;
        }

        return StringUtils.isNotEmpty(entity.getRefreshToken()) && passwordEncoder.matches(refreshToken, entity.getRefreshToken());
    }
}