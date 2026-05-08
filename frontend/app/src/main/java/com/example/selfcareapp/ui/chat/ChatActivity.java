package com.example.selfcareapp.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.selfcareapp.R;
import com.example.selfcareapp.ui.BaseActivity;
import com.example.selfcareapp.ui.SettingsActivity;

/**
 * Entry screen for the chat module.
 *
 * Responsibilities:
 * - Displays the chat landing page
 * - Navigates to the active conversation screen
 * - Provides access to application settings
 */
public class ChatActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        findViewById(R.id.imgSettingsIcon).setOnClickListener(view ->
                startActivity(new Intent(this, SettingsActivity.class)));
    }

    /**
     * Opens the active chat conversation screen.
     */
    public void onStartChatClicked(View view) {
        Intent intent = new Intent(this, ChatConversationActivity.class);
        startActivity(intent);
    }

    /**
     * Reserved for future settings navigation handling.
     */
    public void onSettingsClicked(View view) {
    }
}