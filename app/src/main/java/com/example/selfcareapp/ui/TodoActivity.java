package com.example.selfcareapp.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.selfcareapp.R;

public class TodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Engedélyezi az Edge-to-Edge megjelenítést
        EdgeToEdge.enable(this);

        // Beállítja az activity-hez tartozó layout-ot (most a To-Do list képernyő)
        setContentView(R.layout.activity_todo);

        // Listener a fő nézethez (R.id.main), ami kezeli a rendszer sávok insets-eit
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.todo_root), (v, insets) -> {
            // Lekéri a státusz- és navigációs sávok méreteit
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Beállítja a padding-ot, hogy a tartalom ne lógjon a sávokra
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets; // Visszaadja az insets-et további feldolgozásra
        });
    }
}