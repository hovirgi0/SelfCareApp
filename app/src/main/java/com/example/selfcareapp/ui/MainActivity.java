package com.example.selfcareapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.selfcareapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Engedélyezi az Edge-to-Edge megjelenítést
        EdgeToEdge.enable(this);

        // Beállítja az activity-hez tartozó layout-ot (itt a főképernyő)
        setContentView(R.layout.activity_main);

        // Listener a fő nézethez (R.id.main), ami kezeli a rendszer sávok insets-eit
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Itt állítjuk be az Activity-hez tartozó layout-ot
       /* setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_todo);
        setContentView(R.layout.activity_journal);
        setContentView(R.layout.activity_chat); */

        // Gombok lekérése a layoutból
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnTodo = findViewById(R.id.btnTodo);
        Button btnJournal = findViewById(R.id.btnJournal);
        Button btnChat = findViewById(R.id.btnChat);

        // Gombokhoz kattintás események: új Activity indítása
        btnLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        btnTodo.setOnClickListener(v -> startActivity(new Intent(this, TodoActivity.class)));
        btnJournal.setOnClickListener(v -> startActivity(new Intent(this, JournalActivity.class)));
        btnChat.setOnClickListener(v -> startActivity(new Intent(this, ChatActivity.class)));
    }
}