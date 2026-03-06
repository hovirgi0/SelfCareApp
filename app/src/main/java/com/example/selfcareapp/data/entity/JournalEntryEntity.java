package com.example.selfcareapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "journal_entries")
public class JournalEntryEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;

    public String content;

    public long date;

    public int userId;
}