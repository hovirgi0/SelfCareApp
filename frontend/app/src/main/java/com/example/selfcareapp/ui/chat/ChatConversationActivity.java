package com.example.selfcareapp.ui.chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.selfcareapp.R;
import com.google.gson.JsonObject;
import okhttp3.*;
import com.example.selfcareapp.ui.chat.SentimentEngine;

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
    private ImageView viewSentimentIcon; // SentimentEngine

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_conversation);

        etMessage = findViewById(R.id.etChatMessage);
        rvChat = findViewById(R.id.rvChatHistory);

        // SentimentEngine
        viewSentimentIcon = findViewById(R.id.SentimentIconSmall);

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

        setupTextWatcher();

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

    // --- metódus ---
    /**
     * TextWatcher: valós idejű hangulatfigyelés gépelés közben.
     * Feminist HCI – Agency: a rendszer nem vár, hanem reagál a felhasználóra.
     * A score-t az updateHeaderColor() veszi át.
     */
    private void setupTextWatcher() {
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String currentText = s.toString().trim();
                SentimentEngine.Sentiment sentiment = SentimentEngine.analyze(currentText);
                updateHeaderColor(sentiment);
            }
        });
    }

    /**
     * A chat toolbar háttérszínét a felhasználó aktuális hangulata alapján módosítja.
     *
     * Feminist HCI – Empowerment + Transparency:
     *   A felület vizuálisan visszatükrözi, hogy a rendszer "hallja" a felhasználót.
     *   Nem ítélkezik – mindhárom szín pozitív, befogadó tónus.
     *
     * Színek (Material Theme-ből):
     *   POSITIVE → colorTertiary (meleg, örömteli)
     *   NEGATIVE → colorSecondary (nyugtató, nem riasztó – tudatos design döntés)
     *   NEUTRAL  → colorPrimary (alapszín)
     */
    private void updateHeaderColor(SentimentEngine.Sentiment sentiment) {
        if (viewSentimentIcon == null) return;

        int colorAttr;
        switch (sentiment) {
            case POSITIVE:
                colorAttr = com.google.android.material.R.attr.colorTertiary;
                break;
            case NEGATIVE:
                colorAttr = com.google.android.material.R.attr.colorSecondary;
                break;
            default:
                colorAttr = com.google.android.material.R.attr.colorPrimary;
                break;
        }

        // Material attr → konkrét szín kinyerése a témából
        int resolvedColor = com.google.android.material.color.MaterialColors.getColor(viewSentimentIcon, colorAttr);

    // Apply the color as a tint to the icon
        viewSentimentIcon.setColorFilter(resolvedColor, android.graphics.PorterDuff.Mode.SRC_IN);

        // Optional: Add a subtle scale pulse to show "thinking"
        viewSentimentIcon.animate()
                .scaleX(1.1f).scaleY(1.1f)
                .setDuration(150)
                .withEndAction(() -> viewSentimentIcon.animate().scaleX(1.0f).scaleY(1.0f).start())
                .start();
    }

    /**
     * Beállítások elérése a beszélgetés közben is.
     */
    public void onSettingsClicked(View view) {
        // Opcionális: AI tónus vagy stílus váltása
    }
}