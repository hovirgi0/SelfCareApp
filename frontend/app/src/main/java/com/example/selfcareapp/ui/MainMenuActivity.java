package com.example.selfcareapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.selfcareapp.R;
import com.example.selfcareapp.data.UserPreferences;
import com.example.selfcareapp.ui.chat.ChatActivity;
import com.example.selfcareapp.ui.journal.JournalActivity;
import com.example.selfcareapp.ui.todo.ToDoListActivity;


public class MainMenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //apply theme (without a flash: add before super.onCreate
       // SettingsActivity.restoreTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        //Display saved name
        UserPreferences userPreferences = new UserPreferences(this);
        String name = userPreferences.getPrefsName();
        TextView tvGreeting = findViewById(R.id.tvMenuGreeting);
        if (tvGreeting != null && !name.isEmpty()) {
            tvGreeting.setText("Szia, " + name + "!");
        }

        // Navigate to the Settings Screen
        findViewById(R.id.imgSettingsIcon).setOnClickListener(view ->
                startActivity(new Intent(this, SettingsActivity.class)));

        // Navigate to the To-Do list screen
        // findViewById links the Java code to the button defined in activity_mainmenu.xml
        // setOnClickListener replaces the xml android:onClick approach for consistency
        findViewById(R.id.btnTasks).setOnClickListener(view ->
                startActivity(new Intent(this, ToDoListActivity.class)));

        // Navigate to the Journal screen
        findViewById(R.id.btnJournal).setOnClickListener(view ->
                startActivity(new Intent(this, JournalActivity.class)));

        // Navigate to the Chat screen
        findViewById(R.id.btnChat).setOnClickListener(view ->
                startActivity(new Intent(this, ChatActivity.class)));
    }

    /*public void onSettingsClicked(View view) {
        // empty
    }
    public void onTodoClicked(View view) {
        Intent intent = new Intent(this, ToDoListActivity.class);
        startActivity(intent);
    }

    public void onJournalClicked(View view) {
        Intent intent = new Intent(this, JournalActivity.class);
        startActivity(intent);
    }

    public void onChatClicked(View view) {
        // empty
    }

    public void onButtonClicked(View view) {
        // empty
    }
     */
}


