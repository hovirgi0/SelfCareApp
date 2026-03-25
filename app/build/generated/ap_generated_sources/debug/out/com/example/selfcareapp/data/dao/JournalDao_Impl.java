package com.example.selfcareapp.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.selfcareapp.data.entity.JournalEntryEntity;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class JournalDao_Impl implements JournalDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<JournalEntryEntity> __insertionAdapterOfJournalEntryEntity;

  private final EntityDeletionOrUpdateAdapter<JournalEntryEntity> __deletionAdapterOfJournalEntryEntity;

  private final EntityDeletionOrUpdateAdapter<JournalEntryEntity> __updateAdapterOfJournalEntryEntity;

  public JournalDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfJournalEntryEntity = new EntityInsertionAdapter<JournalEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `journal_entries` (`id`,`mood`,`title`,`content`,`date`,`userId`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final JournalEntryEntity entity) {
        statement.bindLong(1, entity.id);
        if (entity.mood == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.mood);
        }
        if (entity.title == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.title);
        }
        if (entity.content == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.content);
        }
        statement.bindLong(5, entity.date);
        statement.bindLong(6, entity.userId);
      }
    };
    this.__deletionAdapterOfJournalEntryEntity = new EntityDeletionOrUpdateAdapter<JournalEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `journal_entries` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final JournalEntryEntity entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfJournalEntryEntity = new EntityDeletionOrUpdateAdapter<JournalEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `journal_entries` SET `id` = ?,`mood` = ?,`title` = ?,`content` = ?,`date` = ?,`userId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final JournalEntryEntity entity) {
        statement.bindLong(1, entity.id);
        if (entity.mood == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.mood);
        }
        if (entity.title == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.title);
        }
        if (entity.content == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.content);
        }
        statement.bindLong(5, entity.date);
        statement.bindLong(6, entity.userId);
        statement.bindLong(7, entity.id);
      }
    };
  }

  @Override
  public void insertEntry(final JournalEntryEntity entry) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfJournalEntryEntity.insert(entry);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteEntry(final JournalEntryEntity entry) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfJournalEntryEntity.handle(entry);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void editEntry(final JournalEntryEntity editedentry) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfJournalEntryEntity.handle(editedentry);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<JournalEntryEntity> getEntriesForUser(final int userId) {
    final String _sql = "SELECT * FROM journal_entries WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfMood = CursorUtil.getColumnIndexOrThrow(_cursor, "mood");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final List<JournalEntryEntity> _result = new ArrayList<JournalEntryEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final JournalEntryEntity _item;
        _item = new JournalEntryEntity();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfMood)) {
          _item.mood = null;
        } else {
          _item.mood = _cursor.getString(_cursorIndexOfMood);
        }
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _item.title = null;
        } else {
          _item.title = _cursor.getString(_cursorIndexOfTitle);
        }
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _item.content = null;
        } else {
          _item.content = _cursor.getString(_cursorIndexOfContent);
        }
        _item.date = _cursor.getLong(_cursorIndexOfDate);
        _item.userId = _cursor.getInt(_cursorIndexOfUserId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
