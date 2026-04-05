package com.example.selfcareapp.ui.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.selfcareapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChatConversationActivity extends AppCompatActivity {
    private final List<ChatMessage> messages = new ArrayList<>();
    private ChatAdapter adapter;
    private EditText etMessage;
    private RecyclerView rvChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_conversation);

        etMessage = findViewById(R.id.etChatMessage);
        rvChat = findViewById(R.id.rvChatHistory);

        adapter = new ChatAdapter(messages);
        rvChat.setAdapter(adapter);
        rvChat.setLayoutManager(new LinearLayoutManager(this));

        String firstMessage = getIntent().getStringExtra("first_message");
        if (firstMessage != null && !firstMessage.isEmpty()) {
            messages.add(new ChatMessage(firstMessage, ChatMessage.TYPE_USER));
            messages.add(new ChatMessage("Köszönöm az üzeneted!", ChatMessage.TYPE_BOT));
            adapter.notifyDataSetChanged();
            rvChat.scrollToPosition(messages.size() - 1);
        }

    }

    /**
     * Újabb üzenet küldése a már megkezdett beszélgetésben.
     */
    public void onSendMessageClicked(View view) {
        // Itt fogjuk később frissíteni a RecyclerView-t
        // Week 4: Csak a UI gomb működését ellenőrizzük

        String userText = etMessage.getText().toString().trim();
        if (userText.isEmpty()) return;

        // Felhasználó üzenete
        messages.add(new ChatMessage(userText, ChatMessage.TYPE_USER));
        adapter.notifyItemInserted(messages.size() - 1);
        rvChat.scrollToPosition(messages.size() - 1);
        etMessage.setText("");

        // Placeholder bot válasz (API hívás helyett egyelőre)
        messages.add(new ChatMessage("Köszönöm az üzeneted!", ChatMessage.TYPE_BOT));
        adapter.notifyItemInserted(messages.size() - 1);
        rvChat.scrollToPosition(messages.size() - 1);
    }

    /**
     * Beállítások elérése a beszélgetés közben is.
     */
    public void onSettingsClicked(View view) {
        // Opcionális: AI tónus vagy stílus váltása
    }
}