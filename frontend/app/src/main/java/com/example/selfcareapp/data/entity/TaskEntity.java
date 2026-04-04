package com.example.selfcareapp.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class TaskEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String taskName;


    public boolean isCompleted;

    public long createdAt;

    public int userId;
    
    @ColumnInfo(name = "task_order")
    public int order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean isCompleted() {
        return isCompleted;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
