package com.formalhaut.fmh.repository;

import com.formalhaut.fmh.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserRepository {
    @Select("select * from users where username = #{username}")
    UserEntity findByUsername(String username);

    @Select("select * from users where id = #{id}")
    UserEntity findByUserId(Integer id);
}
