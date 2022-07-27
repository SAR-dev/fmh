package com.formalhaut.fmh.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserIssueToken {
    private String accessToken;
    private String refreshToken;
}
