package com.example.selfcareapp.ui.personalization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.selfcareapp.R;
import com.example.selfcareapp.data.UserPreferences;
import com.example.selfcareapp.ui.BaseActivity;
import com.example.selfcareapp.ui.SettingsActivity;
import com.google.android.material.textfield.TextInputEditText;

public class NamePersonalizationActivity extends BaseActivity {

    private TextInputEditText etName; //EditText Name
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //apply theme (without a flash: call before super.onCreate)
       // SettingsActivity.restoreTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalization_name);

        etName = findViewById(R.id.etName);
        userPreferences = new UserPreferences(this);
    }

    public void onNameContinueClicked(View view) {
        String name = etName.getText()!= null ? etName.getText().toString().trim(): "";

        if (name.isEmpty()){
            Toast.makeText(this, "Add meg a neved!", Toast.LENGTH_SHORT).show();
            return;
        }

        userPreferences.savePrefsName(name);

        Intent intent = new Intent(this, PreferencesPersonalizationActivity.class);
        startActivity(intent);
    }
}
