package com.example.selfcareapp.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.selfcareapp.data.entity.UserEntity;

/**
 * Data Access Object for user management.
 * Provides methods for registration and profile retrieval.
 */
@Dao
public interface UserDao {

    /** Registers a new user in the local database. */
    @Insert
    void insertUser(UserEntity user);

    /** Fetches a user profile based on their unique ID. */
    @Query("SELECT * FROM users WHERE id = :userId")
    UserEntity getUserById(int userId);
}