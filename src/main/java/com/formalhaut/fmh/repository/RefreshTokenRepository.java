package com.formalhaut.fmh.repository;

import com.formalhaut.fmh.entity.RefreshTokenEntity;
import com.formalhaut.fmh.entity.UserEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RefreshTokenRepository {

    @Select("select * from refreshTokens where id = #{id}")
    RefreshTokenEntity findById(Integer Id);

    @Select("select * from refreshTokens where token = #{token}")
    RefreshTokenEntity findByToken(String token);

    @Insert("insert into refreshTokens(user_id, token, expiry_date) values(#{user_id}, #{token}), #{expiry_date}")
    void save(RefreshTokenEntity refreshTokenEntity);

    @Delete("select * from refreshTokens where token = #{token}")
    void delete(String token);

    @Delete("select * from refreshTokens where user_id = #{user_id}")
    void deleteByUser(Integer user_id);

}
