package com.example.selfcareapp.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.selfcareapp.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
    }

    /**
     * Átnavigál a beszélgetős nézetre az üzenetküldő gomb megnyomásakor.
     */
    public void onSendMessageClicked(View view) {
        EditText etMessage = findViewById(R.id.etChatMessage);
        String text = etMessage.getText().toString().trim();
        if (text.isEmpty()) return;

        Intent intent = new Intent(this, ChatConversationActivity.class);
        intent.putExtra("first_message", text);
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