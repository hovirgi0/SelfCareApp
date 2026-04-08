package com.example.selfcareapp.ui.chat;

import java.util.HashMap;
import java.util.Map;

/**
 * SentimentEngine – kulcsszó-alapú hangulatfelismerő osztály.
 *
 * Feminist HCI elvek (Bardzell):
 *   - Agency: a felhasználó saját szavaiból indul ki, nem külső modellből
 *   - Transparency: a logika nem fekete doboz, olvasható és auditálható
 *   - Empowerment: a hangulat felismerése célja a segítségnyújtás, nem az értékelés
 *
 * Nem ML-alapú: szándékos döntés – BSc scope + adatvédelmi megfontolás (Design Justice).
 */
public class SentimentEngine {

    // --- Sentiment kategóriák ---
    public enum Sentiment {
        POSITIVE,   // boldog, örül, klassz stb.
        NEGATIVE,   // stressz, fáradt, rossz stb.
        NEUTRAL     // semleges / nem kategorizálható
    }

    // --- Kulcsszó szótárak score-al súlyozva ---
    // Pozitív: +1 pont/szó
    private static final Map<String, Integer> POSITIVE_KEYWORDS = new HashMap<String, Integer>() {{
        put("boldog", 2);
        put("örülök", 2);
        put("remek", 1);
        put("klassz", 1);
        put("szuper", 2);
        put("jó", 1);
        put("fantasztikus", 2);
        put("motivált", 2);
        put("energikus", 1);
        put("sikerült", 2);
        put("büszke", 2);
        put("öröm", 2);
    }};

    // Negatív: -1 pont/szó
    private static final Map<String, Integer> NEGATIVE_KEYWORDS = new HashMap<String, Integer>() {{
        put("stressz", 2);
        put("fáradt", 2);
        put("túlterhelt", 3);
        put("nehéz", 1);
        put("rossz", 1);
        put("sír", 3);
        put("ideges", 2);
        put("szorongás", 3);
        put("depresszió", 3);
        put("reménytelen", 3);
        put("nem bírom", 3);
        put("elveszett", 2);
    }};

    /**
     * Üzenet szövegéből kiszámolja az érzelmi pontszámot.
     * @param message a felhasználó üzenete
     * @return int score: pozitív = jó hangulat, negatív = rossz, 0 = semleges
     */
    public static int computeScore(String message) {
        if (message == null || message.isEmpty()) return 0;

        String lower = message.toLowerCase();
        int score = 0;

        for (Map.Entry<String, Integer> entry : POSITIVE_KEYWORDS.entrySet()) {
            if (lower.contains(entry.getKey())) {
                score += entry.getValue();
            }
        }
        for (Map.Entry<String, Integer> entry : NEGATIVE_KEYWORDS.entrySet()) {
            if (lower.contains(entry.getKey())) {
                score -= entry.getValue();
            }
        }
        return score;
    }

    /**
     * Score alapján Sentiment enum-ot ad vissza.
     * Threshold: >= 2 → POSITIVE, <= -2 → NEGATIVE, között → NEUTRAL
     */
    public static Sentiment analyze(String message) {
        int score = computeScore(message);
        if (score >= 2) return Sentiment.POSITIVE;
        if (score <= -2) return Sentiment.NEGATIVE;
        return Sentiment.NEUTRAL;
    }
}