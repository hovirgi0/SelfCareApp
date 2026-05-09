package com.example.selfcareapp.ui.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selfcareapp.R;
import com.example.selfcareapp.ui.BaseActivity;
import com.example.selfcareapp.ui.SettingsActivity;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Manages the user interface and network interactions for the chat feature.
 * Connects to a local FastAPI backend to process messages and uses real-time
 * sentiment analysis to adapt the UI based on the user's emotional tone.
 */
public class ChatConversationActivity extends BaseActivity {

    // 10.0.2.2 is the special alias to your host loopback interface (localhost) in the Android emulator
    private static final String BASE_URL = "http://10.0.2.2:8000/chat";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();
    private final com.google.gson.Gson gson = new com.google.gson.Gson();
    private final List<ChatMessage> messages = new ArrayList<>();

    private ChatAdapter adapter;
    private EditText etMessage;
    private RecyclerView rvChat;
    private ImageView viewSentimentIcon;

    // Tracks the current conversation context with the backend
    private String sessionId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_conversation);

        initializeViews();
        setupRecyclerView();
        handleInitialIntent();
        setupTextWatcher();
        updateSentimentIconColor(SentimentEngine.Sentiment.NEUTRAL);
        setupSuggestionChipsChat();
    }

    private void initializeViews() {
        etMessage = findViewById(R.id.etChatMessage);
        rvChat = findViewById(R.id.rvChatHistory);
        viewSentimentIcon = findViewById(R.id.SentimentIconSmall);

        findViewById(R.id.imgSettingsIcon).setOnClickListener(view ->
                startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void setupRecyclerView() {
        adapter = new ChatAdapter(messages);
        rvChat.setAdapter(adapter);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Checks if the activity was launched with a pre-filled first message
     * (e.g., from a deep link or another activity) and sends it immediately.
     */
    private void handleInitialIntent() {
        String firstMessage = getIntent().getStringExtra("first_message");
        if (firstMessage != null && !firstMessage.isEmpty()) {
            displayUserMessage(firstMessage);
            sendMessage(firstMessage);
        }
    }

    /**
     * Binds suggestion chips to auto-populate and send predefined messages.
     * Includes logic to expand/collapse additional chips to save screen real estate.
     */
    private void setupSuggestionChipsChat() {
        int[] chipIds = {
                R.id.chipChat1, R.id.chipChat2, R.id.chipChat3,
                R.id.chipChat4, R.id.chipChat5
        };

        for (int id : chipIds) {
            com.google.android.material.chip.Chip chip = findViewById(id);
            chip.setOnClickListener(v -> {
                String suggestion = chip.getText().toString();
                displayUserMessage(suggestion);
                sendMessage(suggestion);
                findViewById(R.id.cgChatSuggestions).setVisibility(View.GONE);
            });
        }

        setupChipToggleLogic();
    }

    private void setupChipToggleLogic() {
        com.google.android.material.chip.Chip chipMore = findViewById(R.id.chipChatMore);
        com.google.android.material.chip.Chip chip4 = findViewById(R.id.chipChat4);
        com.google.android.material.chip.Chip chip5 = findViewById(R.id.chipChat5);

        chipMore.setOnClickListener(v -> {
            boolean isExpanded = chip4.getVisibility() == View.VISIBLE;
            if (isExpanded) {
                chip4.setVisibility(View.GONE);
                chip5.setVisibility(View.GONE);
                chipMore.setText("...");
            } else {
                chip4.setVisibility(View.VISIBLE);
                chip5.setVisibility(View.VISIBLE);
                chipMore.setText("↑");
            }
        });
    }

    /**
     * Triggered directly by the UI Send button (via XML onClick attribute).
     */
    public void onSendMessageClicked(View view) {
        String userText = etMessage.getText().toString().trim();
        if (userText.isEmpty()) return;

        displayUserMessage(userText);
        etMessage.setText("");
        sendMessage(userText);
        findViewById(R.id.cgChatSuggestions).setVisibility(View.GONE);
    }

    /**
     * Appends the user's message to the chat UI and scrolls to the bottom.
     */
    private void displayUserMessage(String text) {
        messages.add(new ChatMessage(text, ChatMessage.TYPE_USER));
        adapter.notifyItemInserted(messages.size() - 1);
        rvChat.scrollToPosition(messages.size() - 1);
    }

    /**
     * Asynchronously sends the user's message to the backend via OkHttp POST.
     * Attaches current user preferences (tone and style) to the request payload.
     */
    private void sendMessage(String userText) {
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);

        JsonObject body = new JsonObject();
        body.addProperty("message", userText);
        body.addProperty("tone", prefs.getString("selected_tone", "neutral"));
        body.addProperty("style", prefs.getString("selected_style", "supportive"));
        body.addProperty("session_id", sessionId);

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(RequestBody.create(body.toString(), JSON))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Network callbacks run on a background thread. UI updates must be routed to the main thread.
                runOnUiThread(() -> displayBotMessage("Sajnos nem sikerült kapcsolódni. Próbáld újra!"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    runOnUiThread(() -> displayBotMessage("Valami hiba történt a szerveren."));
                    return;
                }

                String responseBody = response.body().string();
                JsonObject json = gson.fromJson(responseBody, JsonObject.class);

                sessionId = json.get("session_id").getAsString();
                String botResponse = json.get("response").getAsString();

                runOnUiThread(() -> displayBotMessage(botResponse));
            }
        });
    }

    /**
     * Appends the bot's response to the chat UI and scrolls to the bottom.
     */
    private void displayBotMessage(String text) {
        messages.add(new ChatMessage(text, ChatMessage.TYPE_BOT));
        adapter.notifyItemInserted(messages.size() - 1);
        rvChat.scrollToPosition(messages.size() - 1);
    }

    /**
     * Clears the current session both locally and on the backend.
     */
    private void resetConversation() {
        if (sessionId.isEmpty()) return;

        Request request = new Request.Builder()
                .url(BASE_URL + "/" + sessionId)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handled silently as session will expire on backend eventually
            }

            @Override
            public void onResponse(Call call, Response response) {
                runOnUiThread(() -> {
                    messages.clear();
                    adapter.notifyDataSetChanged();
                    sessionId = "";
                });
            }
        });
    }

    /**
     * Real-time sentiment monitoring while the user is typing.
     * Design Intent (Feminist HCI - Agency): The system reacts passively to the user's input
     * without requiring an explicit submission.
     */
    private void setupTextWatcher() {
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                SentimentEngine.Sentiment sentiment = SentimentEngine.analyze(s.toString().trim());
                updateSentimentIconColor(sentiment);
            }
        });
    }

    /**
     * Updates the chat header's visual state based on the current text sentiment.
     *
     * Design Intent (Feminist HCI - Empowerment/Transparency): Visually reflects that the system
     * is listening without judgment. Colors are mapped to Material Theme semantics:
     * - POSITIVE -> Tertiary (Warm/Joyful)
     * - NEGATIVE -> Secondary (Calming/Non-alarming)
     * - NEUTRAL  -> Primary (Default)
     */
    private void updateSentimentIconColor(SentimentEngine.Sentiment sentiment) {
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

        int resolvedColor = com.google.android.material.color.MaterialColors.getColor(viewSentimentIcon, colorAttr);
        viewSentimentIcon.setColorFilter(resolvedColor, android.graphics.PorterDuff.Mode.SRC_IN);

        // Subtle scale pulse to indicate active processing/listening
        viewSentimentIcon.animate()
                .scaleX(1.1f).scaleY(1.1f)
                .setDuration(150)
                .withEndAction(() -> viewSentimentIcon.animate().scaleX(1.0f).scaleY(1.0f).start())
                .start();
    }
}