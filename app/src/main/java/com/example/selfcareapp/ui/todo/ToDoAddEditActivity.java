package com.example.selfcareapp.ui.todo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.example.selfcareapp.R;

public class ToDoAddEditActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add_edit);
    }

    /**
     * Triggered by the "+" or "Save" icon button next to the input field.
     */
    public void onSaveTaskClicked(View view) {
        // empty on purpose (Week 4 UI lock)
    }

    /**
     * Triggered by the "Mégse" (Cancel) button.
     */
    public void onCancelClicked(View view) {
        // empty on purpose (Week 4 UI lock)
    }
}
