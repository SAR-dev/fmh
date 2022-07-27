package com.formalhaut.fmh.repository;

import com.formalhaut.fmh.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserRepository {
    @Select("select * from users where username = #{username}")
    UserEntity findByUsername(String username);

    @Select("select * from users where id = #{id}")
    UserEntity findByUserId(Integer id);

    @Update("UPDATE users SET refreshToken = #{refreshToken} WHERE id = #{id}")
    boolean updateToken(UserEntity userEntity);

    @Update("UPDATE users SET refreshTokenIssuedAt = #{refreshTokenIssuedAt} WHERE id = #{id}")
    boolean updateTokenIssuedAt(UserEntity userEntity);
}
