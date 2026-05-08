package com.example.selfcareapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room entity storing user-specific application settings.
 */
@Entity(tableName = "user_settings")
public class UserSettingsEntity {

    /**
     * Identifier linking settings to a specific user.
     */
    @PrimaryKey
    public int userId;

    /**
     * Indicates whether notifications are enabled.
     */
    public boolean notificationsEnabled;

    /**
     * Selected application theme.
     */
    public String theme;
}