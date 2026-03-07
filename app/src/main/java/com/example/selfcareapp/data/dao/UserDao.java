package com.example.selfcareapp.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.selfcareapp.data.entity.UserEntity;

@Dao
public interface UserDao {

    @Insert
    void insertUser(UserEntity user);

    @Query("SELECT * FROM users WHERE id = :userId")
    UserEntity getUserById(int userId);
}