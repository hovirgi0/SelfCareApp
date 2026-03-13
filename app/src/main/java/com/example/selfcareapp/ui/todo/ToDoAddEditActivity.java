package com.example.selfcareapp.ui.todo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.selfcareapp.R;
import com.example.selfcareapp.data.entity.TaskEntity;
import com.example.selfcareapp.data.repository.TaskRepository;

public class ToDoAddEditActivity extends Activity {
    private EditText etTaskName;
    private TaskRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add_edit);

        etTaskName = findViewById(R.id.etTaskName);
        repository = new TaskRepository(getApplication());
    }

    /**
     * Triggered by the "+" or "Save" icon button next to the input field.
     */
    //Save new task
    public void onSaveTaskClicked(View view) {
        String title = etTaskName.getText().toString().trim();

        if(title.isEmpty()){
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        TaskEntity task = new TaskEntity();
        task.setTitle(title);
        task.setDescription(""); //etTaskName.getText().toString().trim()
        task.setCreatedAt(System.currentTimeMillis());
        task.setCompleted(false);
        task.setUserId(1);

        new Thread(() -> {
            repository.insertTask(task);

            runOnUiThread(() -> {
                Toast.makeText(this, "Task Saved", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }

    /**
     * Triggered by the "Mégse" (Cancel) button.
     */
    //Cancel adding new Task -> back to main Task Screen
    public void onCancelClicked(View view) {
        finish();
    }
}
