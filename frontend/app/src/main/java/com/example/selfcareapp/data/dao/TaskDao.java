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

    
    @Update
    void updateTask(TaskEntity task);

    @Delete
    void deleteTask(TaskEntity task);

    @Query("SELECT * FROM tasks WHERE userId = :userId ORDER BY task_order ASC")
    List<TaskEntity> getTasksForUser(int userId);

    //in what order the tasks are displayed?
}