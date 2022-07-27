package com.formalhaut.fmh.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class RefreshTokenEntity {
    private Integer user_id;
    private String token;
    private Date expiry_date;
}
