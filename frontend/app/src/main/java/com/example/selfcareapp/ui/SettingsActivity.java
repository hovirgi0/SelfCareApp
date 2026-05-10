package com.example.selfcareapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.selfcareapp.R;
import com.example.selfcareapp.data.UserPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * SettingsActivity facilitates the management of user-defined preferences,
 * specifically focusing on AI personality (tone and style) and visual aesthetics (themes).
 *
 * Implementation Details:
 * - Persistence: Utilizes SharedPreferences for lightweight data storage.
 * - Dynamic UI: Implements manual view state management to provide visual feedback
 *   on selection (active/inactive states).
 * - Theme Engine: Orchestrates runtime theme switching and NightMode synchronization.
 * - Preview Box: Displays a live example message reflecting the current tone + style
 *   combination, sourced from the same response templates used by the chatbot backend.
 */
public class SettingsActivity extends BaseActivity {

    // Persistent storage constants
    private static final String PREFS_NAME = "AppSettings";
    private static final String KEY_THEME = "selected_theme";
    private static final String KEY_TONE = "selected_tone";
    private static final String KEY_STYLE = "selected_style";

    // Preview messages per tone+style combination.
    // Strings are taken verbatim from ARCS["stress"][0] and RESPONSES["stress"]
    // in chatbot.py — stress responses show the clearest personality contrast
    // across all 8 combinations.
    private static final Map<String, String> PREVIEW_MESSAGES;
    static {
        PREVIEW_MESSAGES = new HashMap<>();
        PREVIEW_MESSAGES.put("neutral_supportive",   "Figyelek. Mi az, ami most a legtöbb stresszt okozza?");
        PREVIEW_MESSAGES.put("neutral_direct",        "Értem, hogy most nehéz időszakod van. Miben tudnék leginkább segíteni?");
        PREVIEW_MESSAGES.put("friendly_supportive",   "Látom, hogy nehéz. Mi az, ami most a leginkább nyomja a lelked?");
        PREVIEW_MESSAGES.put("friendly_direct",       "Ne aggódj, majd együtt kibogozzuk. Mi a legnehezebb most?");
        PREVIEW_MESSAGES.put("calm_supportive",       "Vegyél egy mély levegőt. Meséld el kérlek, mi bánt most leginkább.");
        PREVIEW_MESSAGES.put("calm_direct",           "Ez is el fog múlni. Mi az a konkrét dolog, ami most a legjobban bánt?");
        PREVIEW_MESSAGES.put("energetic_supportive",  "Rendben, nézzük meg közelebbről! Mi a legfőbb gond most?");
        PREVIEW_MESSAGES.put("energetic_direct",      "Kemény helyzet, de te még ennél is keményebb vagy. Mit oldjunk meg legelőször?");
    }

    // UI Component References: Tone Selection
    private LinearLayout cardToneNeutral, cardToneFriendly, cardToneCalm, cardToneEnergetic;

    // UI Component References: Interaction Style
    private LinearLayout cardStyleSupportive, cardStyleDirect;

    // UI Component References: Theme Configuration
    private LinearLayout cardThemeDopamineBright, cardThemeDopamineDark, cardThemeSoothingLight, cardThemeSoothingDark;

    // UI Component References: Preview
    private TextView tvPreviewText;
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
         * Theme Injection: The theme must be applied prior to super.onCreate()
         * and setContentView() to ensure the DecorView is inflated with the correct
         * attribute set, preventing visual flickering or layout inconsistencies.
         */
        sharedPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String selectedTheme = sharedPrefs.getString(KEY_THEME, "soothing light");

        switch (selectedTheme) {
            case "dopamine bright": setTheme(R.style.Theme_SelfCareApp_Dopamine_Bright); break;
            case "dopamine dark":   setTheme(R.style.Theme_SelfCareApp_Dopamine_Dark); break;
            case "soothing dark":   setTheme(R.style.Theme_SelfCareApp_Soothing_Dark); break;
            default:                setTheme(R.style.Theme_SelfCareApp_Soothing_Light); break;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Standard initialization sequence
        bindViews();
        setupGreeting();
        loadSelections();
        setOnClicklisteners();
    }

    /**
     * Retrieves the user's name from persistent storage (SharedPreferences)
     * and updates the greeting message dynamically.
     */
    private void setupGreeting() {
        UserPreferences userPreferences = new UserPreferences(this);
        String name = userPreferences.getPrefsName();
        TextView tvGreeting = findViewById(R.id.tvSettingsGreeting);

        if (tvGreeting != null && !name.isEmpty()) {
            tvGreeting.setText("Szia, " + name + "!");
        }
    }

    /**
     * Initializes view references by mapping XML identifiers to Java objects.
     */
    private void bindViews() {
        cardToneNeutral = findViewById(R.id.cardToneNeutral);
        cardToneFriendly = findViewById(R.id.cardToneFriendly);
        cardToneCalm = findViewById(R.id.cardToneCalm);
        cardToneEnergetic = findViewById(R.id.cardToneEnergetic);

        cardStyleSupportive = findViewById(R.id.cardStyleSupportive);
        cardStyleDirect = findViewById(R.id.cardStyleDirect);

        cardThemeDopamineBright = findViewById(R.id.cardThemeDopamineBright);
        cardThemeDopamineDark = findViewById(R.id.cardThemeDopamineDark);
        cardThemeSoothingLight = findViewById(R.id.cardThemeSoothingLight);
        cardThemeSoothingDark = findViewById(R.id.cardThemeSoothingDark);

        tvPreviewText = findViewById(R.id.tvPreviewText);
    }

    /**
     * Restores UI state from persistent storage to ensure the interface reflects
     * the user's current configuration.
     */
    private void loadSelections() {
        highlightTone(sharedPrefs.getString(KEY_TONE,"neutral"));
        highlightStyle(sharedPrefs.getString(KEY_STYLE, "supportive"));
        highlightTheme(sharedPrefs.getString(KEY_THEME, "soothing light"));
        updatePreview();
    }

    /**
     * Assigns event listeners to UI components to handle user interaction.
     */
    private void setOnClicklisteners() {
        cardToneNeutral.setOnClickListener(view -> selectTone("neutral"));
        cardToneFriendly.setOnClickListener(view -> selectTone("friendly"));
        cardToneCalm.setOnClickListener(view ->  selectTone("calm"));
        cardToneEnergetic.setOnClickListener(view -> selectTone("energetic"));

        cardStyleSupportive.setOnClickListener(view -> selectStyle("supportive"));
        cardStyleDirect.setOnClickListener(view -> selectStyle("direct"));

        cardThemeDopamineBright.setOnClickListener(view -> selectTheme("dopamine bright"));
        cardThemeDopamineDark.setOnClickListener(view -> selectTheme("dopamine dark"));
        cardThemeSoothingLight.setOnClickListener(view -> selectTheme("soothing light"));
        cardThemeSoothingDark.setOnClickListener(view -> selectTheme("soothing dark"));
    }

    /**
     * Persists the selected AI tone and updates the visual state of the selection group.
     */
    private void selectTone(String tone) {
        sharedPrefs.edit().putString(KEY_TONE, tone).apply();
        highlightTone(tone);
        updatePreview();
    }

    /**
     * Persists the selected AI interaction style and updates UI feedback.
     */
    private void selectStyle(String style) {
        sharedPrefs.edit().putString(KEY_STYLE, style).apply();
        highlightStyle(style);
        updatePreview();
    }

    /**
     * Handles the theme transition logic.
     * Note: Calling recreate() is necessary to refresh the activity's resource
     * context and apply the new style dynamically.
     */
    private void selectTheme(String theme) {
        sharedPrefs.edit().putString(KEY_THEME, theme).apply();
        highlightTheme(theme);
        applyTheme(theme);

        recreate();
    }

    /**
     * Resets the visual state of all tone-related components and highlights the active selection.
     */
    private void highlightTone(String selected) {
        resetCard(cardToneNeutral);
        resetCard(cardToneFriendly);
        resetCard(cardToneCalm);
        resetCard(cardToneEnergetic);
        switch (selected) {
            case "neutral": activateCard(cardToneNeutral); break;
            case "friendly": activateCard(cardToneFriendly); break;
            case "calm": activateCard(cardToneCalm); break;
            case "energetic": activateCard(cardToneEnergetic); break;
        }
    }

    /**
     * Manages the active state visualization for interaction style options.
     */
    private void highlightStyle(String selected) {
        resetCard(cardStyleSupportive);
        resetCard(cardStyleDirect);
        switch (selected) {
            case "supportive": activateCard(cardStyleSupportive); break;
            case "direct": activateCard(cardStyleDirect); break;
        }
    }

    /**
     * Manages the active state visualization for theme selection options.
     */
    private void highlightTheme(String selected) {
        resetCard(cardThemeDopamineBright);
        resetCard(cardThemeDopamineDark);
        resetCard(cardThemeSoothingLight);
        resetCard(cardThemeSoothingDark);

        switch (selected) {
            case "dopamine bright": activateCard(cardThemeDopamineBright); break;
            case "dopamine dark": activateCard(cardThemeDopamineDark); break;
            case "soothing light": activateCard(cardThemeSoothingLight); break;
            case "soothing dark": activateCard(cardThemeSoothingDark); break;
        }
    }

    /**
     * Updates the preview box to show an example message for the current tone + style combination.
     * The displayed strings are sourced from the same ARCS and RESPONSES templates
     * used by the chatbot backend (chatbot.py), ensuring the preview accurately
     * represents real chatbot behaviour.
     */
    private void updatePreview() {
        String tone  = sharedPrefs.getString(KEY_TONE,  "neutral");
        String style = sharedPrefs.getString(KEY_STYLE, "supportive");
        String key   = tone + "_" + style;
        String preview = PREVIEW_MESSAGES.get(key);
        if (preview != null && tvPreviewText != null) {
            tvPreviewText.setText(preview);
        }
    }

    /**
     * Reverts a card's background to the default attribute-defined color.
     * Uses TypedValue to programmatically resolve theme-specific attributes.
     */
    private void resetCard(LinearLayout card) {
        if (card != null) {
            TypedValue value = new TypedValue();
            getTheme().resolveAttribute(R.attr.cardDefaultBackground, value, true);
            card.setBackgroundColor(value.data);
        }
    }

    /**
     * Applies a highlight color to indicate a component's active/selected state.
     */
    private void activateCard(LinearLayout card){
        if (card != null) card.setBackgroundColor(0xFFD0E8FF);
    }

    /**
     * Synchronizes the application's NightMode state with the selected theme preference.
     * This leverages AppCompatDelegate to manage global system-level UI modes.
     */
    private void applyTheme(String theme){
        switch (theme) {
            case "soothing dark":
            case "dopamine dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;

            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
    }

    /**
     * Static utility method to restore global theme settings upon application initialization
     * or activity transitions outside of SettingsActivity.
     */
    public static void restoreTheme(android.content.Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String theme = prefs.getString(KEY_THEME, "soothing light");
        if ("soothing dark".equals(theme) || "dopamine dark".equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * Navigates back to the parent activity by destroying the current instance.
     */
    public void onBackClicked(View view) {
        finish();
    }
}