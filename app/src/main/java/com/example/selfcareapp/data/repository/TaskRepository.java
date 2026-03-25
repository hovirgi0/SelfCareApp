package com.example.selfcareapp.data.repository;

import android.app.Application;

import java.util.List;

import com.example.selfcareapp.data.dao.TaskDao;
import com.example.selfcareapp.data.database.AppDatabase;
import com.example.selfcareapp.data.entity.TaskEntity;

/*
 Repository class responsible for managing Task data.

 The repository acts as an abstraction layer between
 the application's UI components and the database.
 It ensures that the UI does not directly access
 the DAO or the database.
*/

public class TaskRepository {

    private TaskDao taskDao;

    /*
     Constructor initializes the DAO using the
     singleton instance of the Room database.
    */
    public TaskRepository(Application application) {

        AppDatabase db = AppDatabase.getInstance(application);

        taskDao = db.taskDao();
    }

    /*
     Retrieves all tasks belonging to a specific user.
    */
    //updates data
    public List<TaskEntity> getTasksForUser(int userId) {
        return taskDao.getTasksForUser(userId);
    }

    /*
     Inserts a new task into the database.
    */
    public void insertTask(TaskEntity task) {
        taskDao.insertTask(task);
    }

    public void editTask(TaskEntity task) {
        taskDao.editTask(task);
    }

    /*
     Deletes a task from the database.
    */
    public void deleteTask(TaskEntity task) {
        taskDao.deleteTask(task);
    }

}