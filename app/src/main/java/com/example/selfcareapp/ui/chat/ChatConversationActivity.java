package com.example.selfcareapp.ui.chat;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.selfcareapp.R;

public class ChatConversationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_conversation);
    }

    /**
     * Újabb üzenet küldése a már megkezdett beszélgetésben.
     */
    public void onSendMessageClicked(View view) {
        // Itt fogjuk később frissíteni a RecyclerView-t
        // Week 4: Csak a UI gomb működését ellenőrizzük
    }

    /**
     * Beállítások elérése a beszélgetés közben is.
     */
    public void onSettingsClicked(View view) {
        // Opcionális: AI tónus vagy stílus váltása
    }
}