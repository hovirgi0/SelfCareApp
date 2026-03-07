package com.example.selfcareapp.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.selfcareapp.data.entity.JournalEntryEntity;

@Dao
public interface JournalDao {

    @Insert
    void insertEntry(JournalEntryEntity entry);

    @Update
    void updateEntry(JournalEntryEntity entry);

    @Delete
    void deleteEntry(JournalEntryEntity entry);

    @Query("SELECT * FROM journal_entries WHERE userId = :userId")
    List<JournalEntryEntity> getEntriesForUser(int userId);
}