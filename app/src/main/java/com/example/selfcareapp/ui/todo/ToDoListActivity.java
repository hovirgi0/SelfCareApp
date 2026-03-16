package com.example.selfcareapp.ui.todo;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.example.selfcareapp.R;
import com.example.selfcareapp.data.entity.TaskEntity;
import com.example.selfcareapp.data.repository.TaskRepository;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ToDoListActivity extends AppCompatActivity {
    //Repository-t és az Adaptert osztályszintű változóba való kiemelése
    private TaskAdapter taskAdapter;
    private TaskRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        //Log test 03.10.

        // Room doesnt allow db operations on the main thread
        /*new Thread(() -> {

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
        }).start(); */

        taskAdapter = new TaskAdapter();
        repository = new TaskRepository(getApplication());

        RecyclerView recyclerView = findViewById(R.id.rvTasks); //activity_todo_list.xml ...
        recyclerView.setLayoutManager(new LinearLayoutManager(ToDoListActivity.this));
        recyclerView.setAdapter(taskAdapter);


        //Attach FAB click listener for flaoting add Button
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(view -> {
            Intent intent = new Intent(this, ToDoAddEditActivity.class);
            startActivity(intent);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                TaskEntity taskToDelete = taskAdapter.getTaskAt(position);

                new Thread(() -> {
                    repository.deleteTask(taskToDelete);

                    List<TaskEntity> tasks = repository.getTasksForUser(1);

                    runOnUiThread(() -> {
                        taskAdapter.setTasks(tasks);
                        taskAdapter.notifyDataSetChanged();
                    });
                }).start();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshTaskList();
    }

    private void refreshTaskList(){
        new Thread(() -> {
            List<TaskEntity> tasks = repository.getTasksForUser(1);

            runOnUiThread(() -> {
                taskAdapter.setTasks(tasks);
                taskAdapter.notifyDataSetChanged();

                //empty state kezelése
                TextView tvEmpty = findViewById(R.id.tvEmptyState);
                if (tasks == null || tasks.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE); //"Nincsenek még teendőid"
                } else {
                    tvEmpty.setVisibility(View.GONE); //"Elrejtjük, ha van adat
                }
            });
        }).start();
    }

    /**
     * Triggered by the Floating Action Button (FAB).
     * Navigates to the Add/Edit screen.
     */
    public void onAddTaskClicked(View view) {
        Intent intent = new Intent(this, ToDoAddEditActivity.class);
        startActivity(intent);
    }



}