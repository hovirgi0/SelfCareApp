package com.example.selfcareapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class TaskEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;

    public String description;

    public boolean isCompleted;

    public long createdAt;

    public int userId;
}
