package com.example.focusbuddyapp.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.focusbuddyapp.data.local.entity.SubTaskEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SubTaskDao_Impl implements SubTaskDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SubTaskEntity> __insertionAdapterOfSubTaskEntity;

  private final EntityDeletionOrUpdateAdapter<SubTaskEntity> __updateAdapterOfSubTaskEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSubTask;

  private final SharedSQLiteStatement __preparedStmtOfToggleSubTask;

  public SubTaskDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSubTaskEntity = new EntityInsertionAdapter<SubTaskEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `subtasks` (`id`,`parent_task_id`,`title`,`is_completed`,`is_urgent`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SubTaskEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getParentTaskId());
        statement.bindString(3, entity.getTitle());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.isUrgent() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
      }
    };
    this.__updateAdapterOfSubTaskEntity = new EntityDeletionOrUpdateAdapter<SubTaskEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `subtasks` SET `id` = ?,`parent_task_id` = ?,`title` = ?,`is_completed` = ?,`is_urgent` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SubTaskEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getParentTaskId());
        statement.bindString(3, entity.getTitle());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.isUrgent() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteSubTask = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM subtasks WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfToggleSubTask = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE subtasks SET is_completed = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertSubTask(final SubTaskEntity subTask,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSubTaskEntity.insertAndReturnId(subTask);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSubTask(final SubTaskEntity subTask,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSubTaskEntity.handle(subTask);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSubTask(final int id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSubTask.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteSubTask.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object toggleSubTask(final int id, final boolean isCompleted,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfToggleSubTask.acquire();
        int _argIndex = 1;
        final int _tmp = isCompleted ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfToggleSubTask.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SubTaskEntity>> getSubTasksForTask(final int taskId) {
    final String _sql = "SELECT * FROM subtasks WHERE parent_task_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, taskId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"subtasks"}, new Callable<List<SubTaskEntity>>() {
      @Override
      @NonNull
      public List<SubTaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfParentTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_task_id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_completed");
          final int _cursorIndexOfIsUrgent = CursorUtil.getColumnIndexOrThrow(_cursor, "is_urgent");
          final List<SubTaskEntity> _result = new ArrayList<SubTaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SubTaskEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpParentTaskId;
            _tmpParentTaskId = _cursor.getInt(_cursorIndexOfParentTaskId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final boolean _tmpIsUrgent;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsUrgent);
            _tmpIsUrgent = _tmp_1 != 0;
            _item = new SubTaskEntity(_tmpId,_tmpParentTaskId,_tmpTitle,_tmpIsCompleted,_tmpIsUrgent);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
