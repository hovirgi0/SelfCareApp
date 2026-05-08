package com.example.selfcareapp.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room entity mapping to the "tasks" table.
 * Each task belongs to a single user and supports manual list reordering via {@code task_order}.
 */
@Entity(tableName = "tasks")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String taskName;
    public boolean isCompleted;
    /** Unix timestamp (ms) set at task creation. */
    public long createdAt;
    /** Foreign key reference to {@link UserEntity#id}. */
    public int userId;

    /** Zero-based display position; updated on drag-and-drop reorder. */
    @ColumnInfo(name = "task_order")
    public int order;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}