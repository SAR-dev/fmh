package com.formalhaut.fmh.entity;

import com.formalhaut.fmh.model.UserResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEntity {
    private int id;
    private String username;
    private String password;
    private int coin;

    public UserResponse toUser(){
        return new UserResponse(username, coin);
    }
}
