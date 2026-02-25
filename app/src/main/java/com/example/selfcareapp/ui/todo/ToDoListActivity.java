package com.example.selfcareapp.ui.todo;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.selfcareapp.R;

public class ToDoListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
    }

    /**
     * Triggered by the Floating Action Button (FAB).
     * Navigates to the Add/Edit screen.
     */
    public void onAddTaskClicked(View view) {
        // empty on purpose (Week 4 UI lock)
    }
}