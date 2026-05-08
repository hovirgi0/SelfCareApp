package com.example.selfcareapp.ui.todo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.selfcareapp.R;
import com.example.selfcareapp.data.entity.TaskEntity;
import com.example.selfcareapp.data.repository.TaskRepository;
import com.example.selfcareapp.ui.BaseActivity;

/**
 * Manages the creation and modification of user tasks.
 * Supports direct text input for custom tasks and interactive suggestion chips
 * for quick-entry of common self-care activities.
 */
public class ToDoAddEditActivity extends BaseActivity {

    private EditText etTaskName;
    private TaskRepository taskRepository;
    private int currentTaskId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add_edit);

        initializeViews();
        handleIncomingIntent();
        setupSuggestionChipsTodo();
    }

    private void initializeViews() {
        etTaskName = findViewById(R.id.etTaskName);
        taskRepository = new TaskRepository(getApplication());
    }

    private void handleIncomingIntent() {
        if (getIntent().hasExtra("TASK_ID")) {
            currentTaskId = getIntent().getIntExtra("TASK_ID", -1);
            etTaskName.setText(getIntent().getStringExtra("TASK_NAME"));
        }
    }

    /**
     * Initializes interaction logic for suggestion chips.
     * Clicking a chip performs an immediate background database insertion.
     * Includes a toggle mechanism to expand or collapse secondary options.
     */
    private void setupSuggestionChipsTodo() {
        int[] chipIds = {
                R.id.chipSuggestion1,
                R.id.chipSuggestion2,
                R.id.chipSuggestion3,
                R.id.chipSuggestion4,
                R.id.chipSuggestion5
        };

        for (int id : chipIds) {
            com.google.android.material.chip.Chip chip = findViewById(id);

            chip.setOnClickListener(v -> {
                String suggestion = chip.getText().toString();

                new Thread(() -> {
                    TaskEntity task = new TaskEntity();
                    task.setTaskName(suggestion);
                    task.setUserId(1);
                    task.setCompleted(false);
                    task.setCreatedAt(System.currentTimeMillis());

                    taskRepository.insertTask(task);

                    runOnUiThread(() -> {
                        Toast.makeText(this,
                                suggestion + " hozzáadva!",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }).start();
            });
        }

        setupChipToggleLogic();
    }

    private void setupChipToggleLogic() {
        com.google.android.material.chip.Chip chipMore = findViewById(R.id.chipMore);
        com.google.android.material.chip.Chip chip4 = findViewById(R.id.chipSuggestion4);
        com.google.android.material.chip.Chip chip5 = findViewById(R.id.chipSuggestion5);

        chipMore.setOnClickListener(v -> {
            boolean expanded = chip4.getVisibility() == View.VISIBLE;

            if (expanded) {
                chip4.setVisibility(View.GONE);
                chip5.setVisibility(View.GONE);
                chipMore.setText("...");
            } else {
                chip4.setVisibility(View.VISIBLE);
                chip5.setVisibility(View.VISIBLE);
                chipMore.setText("↑");
            }
        });
    }

    /**
     * Validates input and persists the task to the local database.
     * Differentiates between creating a new record and updating an existing one
     * based on the current taskId context.
     */
    public void onSaveTaskClicked(View view) {
        String taskName = etTaskName.getText().toString().trim();

        if (taskName.isEmpty()) {
            Toast.makeText(this,
                    "Kérlek adj meg egy teendőt",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            TaskEntity task = new TaskEntity();
            task.setTaskName(taskName);
            task.setUserId(1);
            task.setCompleted(false);

            if (currentTaskId == -1) {
                task.setCreatedAt(System.currentTimeMillis());
                taskRepository.insertTask(task);
            } else {
                task.setId(currentTaskId);
                taskRepository.editTask(task);
            }

            runOnUiThread(() -> {
                Toast.makeText(this,
                        "Teendő sikeresen mentve",
                        Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }

    /**
     * Terminates the current activity without committing changes.
     */
    public void onCancelClicked(View view) {
        finish();
    }
}