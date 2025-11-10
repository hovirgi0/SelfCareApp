package com.example.selfcareapp.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.selfcareapp.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Engedélyezi az Edge-to-Edge megjelenítést (a tartalom kinyúlik a státusz- és navigációs sáv alá)
        EdgeToEdge.enable(this);

        // Beállítja az activity-hez tartozó layout-ot (most a Login képernyő)
        setContentView(R.layout.activity_login);

        // A fő nézethez (R.id.main) listener, ami kezeli a rendszer sávok insets-eit
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Lekéri a rendszer sávok méreteit (status bar, navigation bar)
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Beállítja a padding-ot, hogy a tartalom ne lógjon a sávokra
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets; // Visszaadja az insets-et további feldolgozásra
        });
    }
}