package com.example.selfcareapp.ui.todo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.selfcareapp.R;
import com.example.selfcareapp.data.dao.TaskDao;
import com.example.selfcareapp.data.database.AppDatabase;
import com.example.selfcareapp.data.entity.TaskEntity;

import java.util.List;

public class ToDoListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        //Log test 03.10.

        // Room doesnt allow db operations on the main thread
        new Thread(() -> {

            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            TaskDao taskDao = db.taskDao();

            // Create task
            TaskEntity task = new TaskEntity();
            task.setTitle("Buy groceries");
            task.setDescription("milk, eggs, bread");
            task.setCreatedAt(System.currentTimeMillis());
            task.setCompleted(false);
            task.userId = 1;

            // Insert task
            taskDao.insertTask(task);

            // Query tasks
            List<TaskEntity> tasks = taskDao.getTasksForUser(1);

            // Print to Logcat
            for (TaskEntity t : tasks) {
                Log.d("DB_TEST", "Task loaded: " + t.getTitle());
            }
        }).start();
    }

    /**
     * Triggered by the Floating Action Button (FAB).
     * Navigates to the Add/Edit screen.
     */
    public void onAddTaskClicked(View view) {
        // empty on purpose (Week 4 UI lock)
    }

}