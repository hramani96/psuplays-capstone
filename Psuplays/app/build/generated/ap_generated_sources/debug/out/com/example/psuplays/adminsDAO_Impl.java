package com.example.psuplays;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;

@SuppressWarnings({"unchecked", "deprecation"})
public final class adminsDAO_Impl implements adminsDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Admin> __insertionAdapterOfAdmin;

  private final EntityDeletionOrUpdateAdapter<Admin> __deletionAdapterOfAdmin;

  private final EntityDeletionOrUpdateAdapter<Admin> __updateAdapterOfAdmin;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public adminsDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAdmin = new EntityInsertionAdapter<Admin>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `admins` (`rowid`,`email`,`firstName`,`lastName`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Admin value) {
        stmt.bindLong(1, value.id);
        if (value.email == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.email);
        }
        if (value.firstName == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.firstName);
        }
        if (value.lastName == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.lastName);
        }
      }
    };
    this.__deletionAdapterOfAdmin = new EntityDeletionOrUpdateAdapter<Admin>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `admins` WHERE `rowid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Admin value) {
        stmt.bindLong(1, value.id);
      }
    };
    this.__updateAdapterOfAdmin = new EntityDeletionOrUpdateAdapter<Admin>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `admins` SET `rowid` = ?,`email` = ?,`firstName` = ?,`lastName` = ? WHERE `rowid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Admin value) {
        stmt.bindLong(1, value.id);
        if (value.email == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.email);
        }
        if (value.firstName == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.firstName);
        }
        if (value.lastName == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.lastName);
        }
        stmt.bindLong(5, value.id);
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM admins WHERE rowid = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Admin... admins) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfAdmin.insert(admins);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Admin... user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfAdmin.handleMultiple(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Admin... admin) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfAdmin.handleMultiple(admin);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final int adminId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, adminId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDelete.release(_stmt);
    }
  }

  @Override
  public Admin getById(final int adminId) {
    final String _sql = "SELECT * FROM admins WHERE rowid = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, adminId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "rowid");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfFirstName = CursorUtil.getColumnIndexOrThrow(_cursor, "firstName");
      final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "lastName");
      final Admin _result;
      if(_cursor.moveToFirst()) {
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final String _tmpEmail;
        _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        final String _tmpFirstName;
        _tmpFirstName = _cursor.getString(_cursorIndexOfFirstName);
        final String _tmpLastName;
        _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
        _result = new Admin(_tmpId,_tmpEmail,_tmpFirstName,_tmpLastName);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
