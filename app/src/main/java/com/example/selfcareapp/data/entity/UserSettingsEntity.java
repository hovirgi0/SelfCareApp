package com.example.selfcareapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_settings")
public class UserSettingsEntity {

    @PrimaryKey
    public int userId;

    public boolean notificationsEnabled;

    public String theme;
}