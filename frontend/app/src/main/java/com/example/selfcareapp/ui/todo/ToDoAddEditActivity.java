package com.example.selfcareapp.ui.todo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.selfcareapp.R;
import com.example.selfcareapp.data.entity.TaskEntity;
import com.example.selfcareapp.data.repository.TaskRepository;
import com.example.selfcareapp.ui.BaseActivity;
import com.example.selfcareapp.ui.SettingsActivity;

/**
 * Activity for creating a new task or editing an existing one.
 *
 * The screen has two ways to add a task:
 *   1. Manual input: user types in the EditText and taps the "+" button
 *   2. Suggestion chips: pre-defined quick-add options (tap to instantly save)
 *
 * If launched with TASK_ID and TASK_NAME extras, it enters edit mode.
 * Otherwise it creates a new task.
 *
 * @see TaskRepository for database operations (insert, edit)
 * @see TaskEntity for the data model
 */
public class ToDoAddEditActivity extends BaseActivity {

    /**
     * The text input field where the user types a task name manually.
     * Also used internally by onSaveTaskClicked() to read the task name.
     *
     * Declared as a field (not local variable in onCreate) so that
     * onSaveTaskClicked() and setupSuggestionChips() can both access it.
     */
    private EditText etTaskName;

    /**
     * Handles all database operations for tasks.
     * Uses the Repository pattern — the Activity doesn't talk to the DB directly,
     * it goes through TaskRepository which abstracts the DAO layer.
     *
     * @see com.example.selfcareapp.data.repository.TaskRepository
     * @see com.example.selfcareapp.data.dao.TaskDao
     */
    private TaskRepository taskRepository;

    /**
     * Tracks whether we're editing an existing task or creating a new one.
     * -1 is the sentinel value meaning "no task loaded yet" = new task mode.
     * Set from Intent extra TASK_ID when the user taps the edit button on a task.
     *
     * @see com.example.selfcareapp.ui.todo.TaskAdapter — passes TASK_ID via Intent
     */
    private int currentTaskId = -1;

    /**
     * Called when the activity is first created.
     * Sets up the layout, initializes the repository, checks for edit mode,
     * and wires up the suggestion chips.
     *
     * @param savedInstanceState unused here, but would restore state after
     *                           the system kills and recreates the activity
     *                           (e.g. screen rotation)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //apply theme (without a flash: call before super.onCreate)
        // SettingsActivity.restoreTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add_edit);

        // Bind the EditText view from the XML layout
        etTaskName = findViewById(R.id.etTaskName);

        // Initialize repository — needs Application context for Room database access
        taskRepository = new TaskRepository(getApplication());

        // Edit mode: if the adapter passed a task ID and name via Intent extras,
        // pre-fill the input field so the user can modify the existing task name
        if (getIntent().hasExtra("TASK_ID")) {
            currentTaskId = getIntent().getIntExtra("TASK_ID", -1);
            etTaskName.setText(getIntent().getStringExtra("TASK_NAME"));
        }

        // Wire up suggestion chips defined in activity_todo_add_edit.xml
        setupSuggestionChips();
    }

    /**
     * Sets up click listeners for all suggestion chips and the "..." toggle chip.
     *
     * Visible chips (1–3): always shown, clicking one directly saves a task
     * Hidden chips (4–5): start with visibility="gone" in XML, revealed by "..." chip
     * Toggle chip ("..."): shows/hides chips 4 and 5, changes label to "↑" when expanded
     *
     * Why save directly instead of filling the input field?
     * Suggestion chips are meant for quick one-tap adding — no editing needed.
     * The manual input field already handles the "type and save" flow separately.
     *
     * Threading: Room does not allow database operations on the main (UI) thread.
     * We use new Thread() for the insert, then runOnUiThread() to show the Toast
     * and call finish() — because UI updates must happen on the main thread.
     *
     * @see TaskEntity
     * @see TaskRepository#insertTask(TaskEntity)
     */
    private void setupSuggestionChips() {

        // All chips that should save a task when tapped (visible + hidden ones)
        int[] chipIds = {
                R.id.chipSuggestion1,
                R.id.chipSuggestion2,
                R.id.chipSuggestion3,
                R.id.chipSuggestion4, // hidden by default, revealed by "..." chip
                R.id.chipSuggestion5  // hidden by default, revealed by "..." chip
        };

        for (int id : chipIds) {
            com.google.android.material.chip.Chip chip = findViewById(id);
            chip.setOnClickListener(v -> {
                // Read the chip's label as the task name
                String suggestion = chip.getText().toString();

                // Insert on a background thread — Room forbids main thread DB access
                new Thread(() -> {
                    TaskEntity task = new TaskEntity();
                    task.setTaskName(suggestion);
                    task.setUserId(1);       // hardcoded single-user for now
                    task.setCompleted(false);
                    task.setCreatedAt(System.currentTimeMillis()); // Unix timestamp in ms
                    taskRepository.insertTask(task);

                    // Switch back to main thread for UI updates
                    runOnUiThread(() -> {
                        Toast.makeText(ToDoAddEditActivity.this,
                                suggestion + " hozzáadva!", Toast.LENGTH_SHORT).show();
                        finish(); // closes this activity and returns to the task list
                    });
                }).start();
            });
        }

        // "..." toggle chip — shows or hides the extra suggestion chips
        com.google.android.material.chip.Chip chipMore = findViewById(R.id.chipMore);
        com.google.android.material.chip.Chip chip4 = findViewById(R.id.chipSuggestion4);
        com.google.android.material.chip.Chip chip5 = findViewById(R.id.chipSuggestion5);

        chipMore.setOnClickListener(v -> {
            // Check current state by reading chip4's visibility
            boolean isExpanded = chip4.getVisibility() == View.VISIBLE;
            if (isExpanded) {
                // Collapse: hide the extra chips and reset the toggle label
                chip4.setVisibility(View.GONE);
                chip5.setVisibility(View.GONE);
                chipMore.setText("...");
            } else {
                // Expand: reveal the extra chips and update the toggle label
                chip4.setVisibility(View.VISIBLE);
                chip5.setVisibility(View.VISIBLE);
                chipMore.setText("↑");
            }
        });
    }

    /**
     * Triggered by the "+" ImageButton (android:onClick="onSaveTaskClicked" in XML).
     * Validates the input, then either inserts a new task or updates an existing one.
     *
     * New task:    currentTaskId == -1 → insertTask()
     * Edit task:   currentTaskId != -1 → editTask() with the same ID
     *
     * @param view the view that triggered this (the "+" button) — required by XML onClick
     */
    public void onSaveTaskClicked(View view) {
        String taskName = etTaskName.getText().toString().trim();

        // Validate: don't allow empty task names
        if (taskName.isEmpty()) {
           // etTaskName.setError("Nem maradhat üresen!");
            Toast.makeText(this, "Kérlek adj meg egy teendőt", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            TaskEntity task = new TaskEntity();
            task.setTaskName(taskName);
            task.setUserId(1);
            task.setCompleted(false);

            if (currentTaskId == -1) {
                // New task: set creation timestamp and insert
                task.setCreatedAt(System.currentTimeMillis());
                taskRepository.insertTask(task);
            } else {
                // Edit mode: reuse the existing task's ID so Room updates the right row
                task.setId(currentTaskId);
                taskRepository.editTask(task);
            }

            runOnUiThread(() -> {
                Toast.makeText(ToDoAddEditActivity.this,
                        "Teendő sikeresen mentve", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }

    /**
     * Triggered by the "Mégse" Button (android:onClick="onCancelClicked" in XML).
     * Discards any input and returns to the previous screen (task list).
     *
     * finish() simply closes this activity — no data is saved.
     *
     * @param view the view that triggered this (the cancel button)
     */
    public void onCancelClicked(View view) {
        finish();
    }
}