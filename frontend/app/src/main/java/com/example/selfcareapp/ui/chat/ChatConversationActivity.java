package com.example.selfcareapp.ui.chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.selfcareapp.R;
import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatConversationActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://10.0.2.2:8000/chat";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();
    private final com.google.gson.Gson gson = new com.google.gson.Gson();

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
           /* messages.add(new ChatMessage("Köszönöm az üzeneted!", ChatMessage.TYPE_BOT));
            adapter.notifyDataSetChanged(); */
            adapter.notifyItemInserted(messages.size() - 1);
            rvChat.scrollToPosition(messages.size() - 1);
            sendMessage(firstMessage);
        }

    }

    /**
     * Újabb üzenet küldése a már megkezdett beszélgetésben.
     * OkHttp POST → FastAPI /chat endpoint.
     * Tone + style SharedPreferences-ből olvasva.
     */
    public void onSendMessageClicked(View view) {
        // Itt fogom később frissíteni a RecyclerView-t
        // Week 4: Csak a UI gomb működését ellenőrizzük

        String userText = etMessage.getText().toString().trim();
        if (userText.isEmpty()) return;

        // Felhasználó üzenete
        messages.add(new ChatMessage(userText, ChatMessage.TYPE_USER));
        adapter.notifyItemInserted(messages.size() - 1);
        rvChat.scrollToPosition(messages.size() - 1);
        etMessage.setText("");

        sendMessage(userText);
    }

    /**
     * OkHttp POST → FastAPI /chat endpoint.
     * Tone + style SharedPreferences-ből olvasva.
     */
    private void sendMessage(String userText) {
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String tone = prefs.getString("selected_tone", "neutral");
        String style = prefs.getString("selected_style", "supportive");

        // JSON body összeállítása
        JsonObject body = new JsonObject();
        body.addProperty("message", userText);
        body.addProperty("tone", tone);
        body.addProperty("style", style);

        //chatbot.py modify + must return 'response'
        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(RequestBody.create(body.toString(), JSON))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Hálózati hiba → fallback üzenet a főszálon
                runOnUiThread(() -> addBotMessage("Sajnos nem sikerült kapcsolódni. Próbáld újra!"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    runOnUiThread(() -> addBotMessage("Valami hiba történt a szerveren."));
                    return;
                }

                String responseBody = response.body().string();
                JsonObject json = gson.fromJson(responseBody, JsonObject.class);
                String botResponse = json.get("response").getAsString();

                // UI frissítés a főszálon (OkHttp callback háttérszálon fut)
                runOnUiThread(() -> addBotMessage(botResponse));
            }
        });

        // Placeholder bot válasz (API hívás helyett egyelőre)
       /*
        messages.add(new ChatMessage("Köszönöm az üzeneted!", ChatMessage.TYPE_BOT));
        adapter.notifyItemInserted(messages.size() - 1);
        rvChat.scrollToPosition(messages.size() - 1);
        */
    }

    private void addBotMessage(String text) {
        messages.add(new ChatMessage(text, ChatMessage.TYPE_BOT));
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