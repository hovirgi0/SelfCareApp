package com.example.selfcareapp.ui.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selfcareapp.R;
import com.example.selfcareapp.data.entity.TaskEntity;
import com.example.selfcareapp.data.repository.TaskRepository;
import com.example.selfcareapp.ui.BaseActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Activity responsible for displaying the user's to-do list.
 * Supports task visualization, marking tasks as complete, reordering via drag-and-drop,
 * and deletion via swipe gestures.
 */
public class ToDoListActivity extends BaseActivity {

    private TaskAdapter taskAdapter;
    private TaskRepository taskRepository;
    private RecyclerView rvTasks;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        initializeComponents();
        setupDateTimeHeader();
        setupTaskListeners();
        setupFab();
        setupTouchHelper();
    }

    private void initializeComponents() {
        taskAdapter = new TaskAdapter();
        taskRepository = new TaskRepository(getApplication());

        rvTasks = findViewById(R.id.rvTasks);
        tvEmpty = findViewById(R.id.tvEmptyState);

        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        rvTasks.setAdapter(taskAdapter);
    }

    /**
     * Formats and displays the current date in the header.
     * Uses Locale.getDefault() to ensure the date format respects device language settings.
     */
    private void setupDateTimeHeader() {
        TextView tvDateMetadata = findViewById(R.id.tvDateMetadata);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE MMM d", java.util.Locale.getDefault());
        String currentDateAndTime = sdf.format(new java.util.Date());
        tvDateMetadata.setText(currentDateAndTime);
    }

    /**
     * Binds listeners to the adapter to handle database updates when
     * task status or list order changes.
     */
    private void setupTaskListeners() {
        taskAdapter.setOnTaskStatusChangedListener(task -> {
            new Thread(() -> {
                taskRepository.editTask(task);
            }).start();
        });

        taskAdapter.setOnTaskOrderChangedListener(reorderedTasks -> {
            new Thread(() -> {
                for (TaskEntity task : reorderedTasks) {
                    taskRepository.updateTask(task);
                }
            }).start();
        });
    }

    private void setupFab() {
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(view -> {
            Intent intent = new Intent(this, ToDoAddEditActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Configures ItemTouchHelper to handle both vertical dragging for reordering
     * and horizontal swiping for task deletion.
     */
    private void setupTouchHelper() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                taskAdapter.moveItem(from, to);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                TaskEntity taskToDelete = taskAdapter.getTaskAt(position);

                new Thread(() -> {
                    taskRepository.deleteTask(taskToDelete);
                    refreshTaskList();
                }).start();
            }
        };

        new ItemTouchHelper(callback).attachToRecyclerView(rvTasks);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTaskList();
    }

    /**
     * Asynchronously refreshes the task list from the database.
     * Manages the visibility of the "Empty State" message based on the task count.
     */
    private void refreshTaskList() {
        new Thread(() -> {
            List<TaskEntity> tasks = taskRepository.getTasksForUser(1);

            runOnUiThread(() -> {
                taskAdapter.setTasks(tasks);
                taskAdapter.notifyDataSetChanged();

                if (tasks == null || tasks.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                    rvTasks.setVisibility(View.GONE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                    rvTasks.setVisibility(View.VISIBLE);
                }
            });
        }).start();
    }
}