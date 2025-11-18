package com.example.selfcareapp.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.selfcareapp.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// Engedélyezi az Edge-to-Edge megjelenítést, vagyis a tartalom a rendszer sávjai alá is kinyúlik
        EdgeToEdge.enable(this);

        // Beállítja az activity-hez tartozó layout-ot
        setContentView(R.layout.activity_chat);

        // Listener beállítása, ami kezeli a rendszer sávok (status bar, navigation bar) insets-eit
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chat_root), (v, insets) -> {
            // Lekéri a rendszer sávok méreteit
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Beállítja a nézet padding-ját úgy, hogy a tartalom ne lógjon a rendszer sávokra
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets; // Visszaadja az insets-et további feldolgozásra
        });
    }
}