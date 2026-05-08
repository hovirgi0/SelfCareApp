package com.example.selfcareapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.selfcareapp.R;
import com.example.selfcareapp.data.UserPreferences;
import com.example.selfcareapp.ui.chat.ChatActivity;
import com.example.selfcareapp.ui.journal.JournalActivity;
import com.example.selfcareapp.ui.todo.ToDoListActivity;

/**
 * The main navigation hub of the application.
 * Manages access to different modules and displays personalized user data.
 */
public class MainMenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        setupGreeting();
        setupNavigation();
    }

    /**
     * Retrieves the user's name from persistent storage (SharedPreferences)
     * and updates the greeting message dynamically.
     */
    private void setupGreeting() {
        UserPreferences userPreferences = new UserPreferences(this);
        String name = userPreferences.getPrefsName();
        TextView tvGreeting = findViewById(R.id.tvMenuGreeting);

        if (tvGreeting != null && !name.isEmpty()) {
            tvGreeting.setText("Szia, " + name + "!");
        }
    }

    /**
     * Initializes click listeners for the main menu buttons using explicit Intents.
     */
    private void setupNavigation() {
        // Settings navigation
        findViewById(R.id.imgSettingsIcon).setOnClickListener(view ->
                startActivity(new Intent(this, SettingsActivity.class)));

        // To-Do module navigation
        findViewById(R.id.btnTasks).setOnClickListener(view ->
                startActivity(new Intent(this, ToDoListActivity.class)));

        // Journal module navigation
        findViewById(R.id.btnJournal).setOnClickListener(view ->
                startActivity(new Intent(this, JournalActivity.class)));

        // Chat module navigation
        findViewById(R.id.btnChat).setOnClickListener(view ->
                startActivity(new Intent(this, ChatActivity.class)));
    }
}