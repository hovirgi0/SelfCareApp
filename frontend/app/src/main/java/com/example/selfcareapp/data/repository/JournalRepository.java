package com.example.selfcareapp.data.repository;

import android.app.Application;
import java.util.List;
import com.example.selfcareapp.data.dao.JournalDao;
import com.example.selfcareapp.data.database.AppDatabase;
import com.example.selfcareapp.data.entity.JournalEntryEntity;

/**
 * Repository responsible for handling journal entry data.
 * Acts as an abstraction layer between the database and the UI-related components.
 */
public class JournalRepository {

    private final JournalDao journalDao;

    /**
     * Initializes the DAO using the AppDatabase singleton instance.
     * @param application The application context for database initialization.
     */
    public JournalRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.journalDao = db.journalDao();
    }

    /**
     * Fetches all journal entries linked to a specific user.
     */
    public List<JournalEntryEntity> getEntriesForUser(int userId) {
        return journalDao.getEntriesForUser(userId);
    }

    /**
     * Delegates the insertion of a new journal entry to the DAO.
     */
    public void insertEntry(JournalEntryEntity entry) {
        journalDao.insertEntry(entry);
    }

    /**
     * Updates an existing journal entry's data.
     */
    public void editEntry(JournalEntryEntity entry) {
        journalDao.editEntry(entry);
    }

    /**
     * Removes a specific journal entry from the local storage.
     */
    public void deleteEntry(JournalEntryEntity entry) {
        journalDao.deleteEntry(entry);
    }
}