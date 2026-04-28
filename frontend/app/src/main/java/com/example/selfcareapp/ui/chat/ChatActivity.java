package com.example.selfcareapp.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.selfcareapp.R;
import com.example.selfcareapp.ui.BaseActivity;
import com.example.selfcareapp.ui.SettingsActivity;

public class ChatActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //apply theme (without a flash: call before super.onCreate)
        //SettingsActivity.restoreTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        // Navigate to the Settings Screen
        findViewById(R.id.imgSettingsIcon).setOnClickListener(view ->
                startActivity(new Intent(this, SettingsActivity.class)));
    }

    /**
     * Átnavigál a beszélgetős nézetre a "Kezdjük el" gomb megnyomásakor.
     */
    public void onStartChatClicked(View view) {
        Intent intent = new Intent(this, ChatConversationActivity.class);
        startActivity(intent);
    }

    /**
     * A beállítások (Settings) gombra kattintás kezelője.
     */
    public void onSettingsClicked(View view) {
        // Üres a hétre (Week 4 UI lock)
    }

    /*  Logic -> next week
    // ChatActivity.java-ban
    public void onSettingsClicked(View view) {
        Intent intent = new Intent(this, com.example.selfcareapp.ui.settings.SettingsActivity.class);
        startActivity(intent);
    } */
}