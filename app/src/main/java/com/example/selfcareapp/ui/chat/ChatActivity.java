package com.example.selfcareapp.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        Intent intent = new Intent(this, ChatConversationActivity.class);
        startActivity(intent);
    }

    /**
     * A beállítások (Settings) gombra kattintás kezelője.
     */
    public void onSettingsClicked(View view) {
        // Üres a hétre (Week 4 UI lock)
    }
}