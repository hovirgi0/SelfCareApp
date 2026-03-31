package com.example.selfcareapp.data.repository;

import android.app.Application;

import java.util.List;

import com.example.selfcareapp.data.dao.JournalDao;
import com.example.selfcareapp.data.database.AppDatabase;
import com.example.selfcareapp.data.entity.JournalEntryEntity;
import com.example.selfcareapp.data.entity.TaskEntity;

/*
 Repository responsible for handling journal entry data.

 This class abstracts database access and provides
 a clean API for higher application layers.
*/

public class JournalRepository {

    private JournalDao journalDao;

    /*
     Initializes DAO through the AppDatabase singleton.
    */
    public JournalRepository(Application application) {

        AppDatabase db = AppDatabase.getInstance(application);

        journalDao = db.journalDao();
    }

    /*
     Retrieves all journal entries belonging to a user.
    */
    public List<JournalEntryEntity> getEntriesForUser(int userId) {
        return journalDao.getEntriesForUser(userId);
    }

    /*
     Inserts a new journal entry.
    */
    public void insertEntry(JournalEntryEntity entry) {
        journalDao.insertEntry(entry);
    }

    public void editEntry(JournalEntryEntity entry) {
        journalDao.editEntry(entry);
    }

    /*
     Deletes an existing journal entry.
    */
    public void deleteEntry(JournalEntryEntity entry) {
        journalDao.deleteEntry(entry);
    }

}