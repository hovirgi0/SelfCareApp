package com.example.selfcareapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room entity representing a journal entry stored in the "journal_entries" table.
 * Data is stored locally on-device to preserve user privacy.
 */
@Entity(tableName = "journal_entries")
public class JournalEntryEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    /** Mood associated with the entry (e.g. "happy", "neutral", "😊"). */
    public String mood;

    public String title;

    public String content;

    /** Unix timestamp (milliseconds) indicating when the entry was created. */
    public long date;

    /** Foreign key reference to {@link UserEntity#id}. */
    public int userId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}