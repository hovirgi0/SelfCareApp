package com.example.selfcareapp.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.selfcareapp.data.entity.JournalEntryEntity;

import java.util.List;

/**
 * Data Access Object for journal entries.
 * Provides database operations for creating, updating, deleting,
 * and retrieving user reflection data.
 */
@Dao
public interface JournalDao {

    /** Inserts a new journal entry into the database. */
    @Insert
    void insertEntry(JournalEntryEntity entry);

    /** Updates an existing journal entry (content or mood). */
    @Update
    void editEntry(JournalEntryEntity entry);

    /** Deletes a journal entry permanently from the database. */
    @Delete
    void deleteEntry(JournalEntryEntity entry);

    /**
     * Returns all journal entries belonging to a specific user.
     */
    @Query("SELECT * FROM journal_entries WHERE userId = :userId")
    List<JournalEntryEntity> getEntriesForUser(int userId);
}