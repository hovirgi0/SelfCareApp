package com.example.selfcareapp.data.repository;

import android.app.Application;
import java.util.List;
import com.example.selfcareapp.data.dao.TaskDao;
import com.example.selfcareapp.data.database.AppDatabase;
import com.example.selfcareapp.data.entity.TaskEntity;

/**
 * Repository class that abstracts access to Task data sources.
 * It acts as a mediator between the ViewModel and the Room database.
 */
public class TaskRepository {

    private final TaskDao taskDao;

    /**
     * Initializes the DAO using the AppDatabase singleton.
     * @param application The application context needed for database initialization.
     */
    public TaskRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.taskDao = db.taskDao();
    }

    /**
     * Retrieves tasks associated with a user, sorted by their order.
     */
    public List<TaskEntity> getTasksForUser(int userId) {
        return taskDao.getTasksForUser(userId);
    }

    /**
     * Delegates task insertion to the DAO.
     */
    public void insertTask(TaskEntity task) {
        taskDao.insertTask(task);
    }

    /**
     * Updates an existing task's information.
     */
    public void editTask(TaskEntity task) {
        taskDao.editTask(task);
    }

    /**
     * Updates specific task fields (e.g., completion status).
     */
    public void updateTask(TaskEntity task) {
        taskDao.updateTask(task);
    }

    /**
     * Removes a task from the persistent storage.
     */
    public void deleteTask(TaskEntity task) {
        taskDao.deleteTask(task);
    }
}