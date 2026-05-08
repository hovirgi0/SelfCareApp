/**
 * Lightweight keyword-based sentiment analyzer used by the chat UI.
 *
 * Design goals:
 * - Transparent and inspectable logic
 * - No external ML dependency
 * - Minimal data processing
 * - Fast real-time feedback while typing
 *
 * The engine assigns weighted scores to keywords and maps the
 * final score to a sentiment category.
 */
package com.example.selfcareapp.ui.chat;

import java.util.HashMap;
import java.util.Map;

public class SentimentEngine {

    /**
     * High-level emotional categories returned by the analyzer.
     */
    public enum Sentiment {
        POSITIVE,
        NEGATIVE,
        NEUTRAL
    }

    /**
     * Positive keywords with weighted scores.
     * Higher values indicate stronger emotional intensity.
     */
    private static final Map<String, Integer> POSITIVE_KEYWORDS =
            new HashMap<String, Integer>() {{
                put("boldog", 2);
                put("örülök", 2);
                put("szuper", 2);
                put("motivált", 2);
            }};

    /**
     * Negative keywords with weighted scores.
     * Higher absolute values indicate stronger emotional intensity.
     */
    private static final Map<String, Integer> NEGATIVE_KEYWORDS =
            new HashMap<String, Integer>() {{
                put("stressz", 2);
                put("fáradt", 2);
                put("szorongás", 3);
                put("reménytelen", 3);
            }};

    /**
     * Computes a weighted sentiment score from the input text.
     *
     * Positive words increase the score,
     * negative words decrease it.
     *
     * @param message user input text
     * @return aggregated sentiment score
     */
    public static int computeScore(String message) {

        if (message == null || message.isEmpty()) {
            return 0;
        }

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
     * Converts the numeric score into a sentiment category.
     *
     * Thresholds:
     * - score >= 2  → POSITIVE
     * - score <= -2 → NEGATIVE
     * - otherwise   → NEUTRAL
     *
     * @param message user input text
     * @return detected sentiment category
     */
    public static Sentiment analyze(String message) {

        int score = computeScore(message);

        if (score >= 2) {
            return Sentiment.POSITIVE;
        }

        if (score <= -2) {
            return Sentiment.NEGATIVE;
        }

        return Sentiment.NEUTRAL;
    }
}