package com.example.selfcareapp.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.selfcareapp.data.entity.TaskEntity;

@Dao
public interface TaskDao {

    @Insert
    void insertTask(TaskEntity task);

    @Update
    void editTask(TaskEntity editedtask);

    @Delete
    void deleteTask(TaskEntity task);

    @Query("SELECT * FROM tasks WHERE userId = :userId")
    List<TaskEntity> getTasksForUser(int userId);
}