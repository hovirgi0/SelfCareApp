package com.example.selfcareapp.ui.todo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.selfcareapp.R;
import com.example.selfcareapp.data.dao.TaskDao;
import com.example.selfcareapp.data.entity.TaskEntity;
import com.example.selfcareapp.data.repository.TaskRepository;
import com.example.selfcareapp.ui.journal.JournalAddEntryActivity;

public class ToDoAddEditActivity extends Activity {
    private EditText etTaskName;
    private EditText etTaskDescription;
    private TaskRepository taskRepository;
    private int currentTaskId = -1; //-1 jelzi, ha új feladatról van szó

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add_edit);

        etTaskName = findViewById(R.id.etTaskName);
        etTaskDescription = findViewById(R.id.etTaskDescription);
        taskRepository = new TaskRepository(getApplication());

        if (getIntent().hasExtra("TASK_ID")) {
            currentTaskId = getIntent().getIntExtra("TASK_ID", -1);
            etTaskName.setText(getIntent().getStringExtra("TASK_TITLE"));
            etTaskDescription.setText(getIntent().getStringExtra("TASK_DESCRIPTION"));
        }
    }

    /**
     * Triggered by the "+" or "Save" icon button next to the input field.
     */
    //Save new task
    public void onSaveTaskClicked(View view) {
        String title = etTaskName.getText().toString().trim();
        String desc = etTaskDescription.getText().toString().trim();

        //Simple Validation Hibakezelés
        if(title.isEmpty()){
            etTaskName.setError("A cím nem maradhat üresen!"); //Vizuális hibaüzenet
            Toast.makeText(this, "Kérlek adj meg egy címet", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            //új hozzáadása
            TaskEntity task = new TaskEntity();
            task.setTitle(title);
            task.setDescription(desc); //etTaskName.getText().toString().trim()
            task.setUserId(1);
            task.setCompleted(false);

            if (currentTaskId == -1) {
                task.setCreatedAt(System.currentTimeMillis());
                taskRepository.insertTask(task);

            } else {
                //Szerkesztés: létező task-et
                // Itt lehet lekérni a régit és módosítani v. újat küldeni ugyanazzal az id-val
                task.setId(currentTaskId);
                taskRepository.editTask(task);
            }
            //Itt dől el, hogy: Új vagy szerkesztés?
            //repository.insertTask(task); //jelenleg csak Insert van benne


            runOnUiThread(() ->  {
                //Validáció: Bejegyzés sikeresen hozzáadva
                Toast.makeText(ToDoAddEditActivity.this, "Teendő sikeresen mentve", Toast.LENGTH_SHORT).show();
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
