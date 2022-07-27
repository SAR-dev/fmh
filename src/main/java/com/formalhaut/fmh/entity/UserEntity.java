package com.formalhaut.fmh.entity;

import com.formalhaut.fmh.model.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserEntity {
    private int id;
    private String username;
    private String password;
    private String refreshToken;
    private Instant refreshTokenIssuedAt;
}
