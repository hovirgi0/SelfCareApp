package com.example.selfcareapp.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.selfcareapp.data.dao.JournalDao;
import com.example.selfcareapp.data.dao.TaskDao;
import com.example.selfcareapp.data.dao.UserDao;
import com.example.selfcareapp.data.entity.JournalEntryEntity;
import com.example.selfcareapp.data.entity.TaskEntity;
import com.example.selfcareapp.data.entity.UserEntity;
import com.example.selfcareapp.data.entity.UserSettingsEntity;

@Database(
        entities = {
                UserEntity.class,
                TaskEntity.class,
                JournalEntryEntity.class,
                UserSettingsEntity.class
        },
        version = 1
)

public abstract class AppDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

    public abstract JournalDao journalDao();

    public abstract UserDao userDao();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {

        if (instance == null) {

            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "selfcare_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}