package com.example.selfcareapp.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import com.example.selfcareapp.data.entity.TaskEntity;

/**
 * Data Access Object for handling task-related database operations.
 */
@Dao
public interface TaskDao {

    /** Inserts a new task record. */
    @Insert
    void insertTask(TaskEntity task);

    /** Updates task details like name or priority. */
    @Update
    void editTask(TaskEntity editedtask);

    /** Updates the task state (e.g., completion status). */
    @Update
    void updateTask(TaskEntity task);

    /** Removes a specific task from the database. */
    @Delete
    void deleteTask(TaskEntity task);

    /** 
     * Retrieves all tasks for a specific user.
     * Results are ordered by the 'task_order' field in ascending order.
     */
    @Query("SELECT * FROM tasks WHERE userId = :userId ORDER BY task_order ASC")
    List<TaskEntity> getTasksForUser(int userId);
}