package com.example.focusbuddyapp.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.focusbuddyapp.data.local.entity.FocusSessionEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class FocusSessionDao_Impl implements FocusSessionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FocusSessionEntity> __insertionAdapterOfFocusSessionEntity;

  private final EntityDeletionOrUpdateAdapter<FocusSessionEntity> __updateAdapterOfFocusSessionEntity;

  public FocusSessionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFocusSessionEntity = new EntityInsertionAdapter<FocusSessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `focus_sessions` (`id`,`linked_task_id`,`duration_minutes`,`start_time`,`end_time`,`efficiency_percent`,`break_duration_minutes`,`remote_id`,`synced_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FocusSessionEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getLinkedTaskId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getLinkedTaskId());
        }
        statement.bindLong(3, entity.getDurationMinutes());
        statement.bindLong(4, entity.getStartTime());
        if (entity.getEndTime() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getEndTime());
        }
        if (entity.getEfficiencyPercent() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getEfficiencyPercent());
        }
        statement.bindLong(7, entity.getBreakDurationMinutes());
        if (entity.getRemoteId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getRemoteId());
        }
        if (entity.getSyncedAt() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getSyncedAt());
        }
      }
    };
    this.__updateAdapterOfFocusSessionEntity = new EntityDeletionOrUpdateAdapter<FocusSessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `focus_sessions` SET `id` = ?,`linked_task_id` = ?,`duration_minutes` = ?,`start_time` = ?,`end_time` = ?,`efficiency_percent` = ?,`break_duration_minutes` = ?,`remote_id` = ?,`synced_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FocusSessionEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getLinkedTaskId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getLinkedTaskId());
        }
        statement.bindLong(3, entity.getDurationMinutes());
        statement.bindLong(4, entity.getStartTime());
        if (entity.getEndTime() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getEndTime());
        }
        if (entity.getEfficiencyPercent() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getEfficiencyPercent());
        }
        statement.bindLong(7, entity.getBreakDurationMinutes());
        if (entity.getRemoteId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getRemoteId());
        }
        if (entity.getSyncedAt() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getSyncedAt());
        }
        statement.bindLong(10, entity.getId());
      }
    };
  }

  @Override
  public Object insertSession(final FocusSessionEntity session,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfFocusSessionEntity.insertAndReturnId(session);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSession(final FocusSessionEntity session,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfFocusSessionEntity.handle(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSessionById(final int id,
      final Continuation<? super FocusSessionEntity> $completion) {
    final String _sql = "SELECT * FROM focus_sessions WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<FocusSessionEntity>() {
      @Override
      @Nullable
      public FocusSessionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLinkedTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "linked_task_id");
          final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_minutes");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_time");
          final int _cursorIndexOfEfficiencyPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "efficiency_percent");
          final int _cursorIndexOfBreakDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "break_duration_minutes");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "synced_at");
          final FocusSessionEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final Integer _tmpLinkedTaskId;
            if (_cursor.isNull(_cursorIndexOfLinkedTaskId)) {
              _tmpLinkedTaskId = null;
            } else {
              _tmpLinkedTaskId = _cursor.getInt(_cursorIndexOfLinkedTaskId);
            }
            final int _tmpDurationMinutes;
            _tmpDurationMinutes = _cursor.getInt(_cursorIndexOfDurationMinutes);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final Integer _tmpEfficiencyPercent;
            if (_cursor.isNull(_cursorIndexOfEfficiencyPercent)) {
              _tmpEfficiencyPercent = null;
            } else {
              _tmpEfficiencyPercent = _cursor.getInt(_cursorIndexOfEfficiencyPercent);
            }
            final int _tmpBreakDurationMinutes;
            _tmpBreakDurationMinutes = _cursor.getInt(_cursorIndexOfBreakDurationMinutes);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final Long _tmpSyncedAt;
            if (_cursor.isNull(_cursorIndexOfSyncedAt)) {
              _tmpSyncedAt = null;
            } else {
              _tmpSyncedAt = _cursor.getLong(_cursorIndexOfSyncedAt);
            }
            _result = new FocusSessionEntity(_tmpId,_tmpLinkedTaskId,_tmpDurationMinutes,_tmpStartTime,_tmpEndTime,_tmpEfficiencyPercent,_tmpBreakDurationMinutes,_tmpRemoteId,_tmpSyncedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FocusSessionEntity>> getTodaySessions(final long startOfDay,
      final long endOfDay) {
    final String _sql = "SELECT * FROM focus_sessions WHERE start_time >= ? AND start_time < ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfDay);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfDay);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"focus_sessions"}, new Callable<List<FocusSessionEntity>>() {
      @Override
      @NonNull
      public List<FocusSessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLinkedTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "linked_task_id");
          final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_minutes");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_time");
          final int _cursorIndexOfEfficiencyPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "efficiency_percent");
          final int _cursorIndexOfBreakDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "break_duration_minutes");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "synced_at");
          final List<FocusSessionEntity> _result = new ArrayList<FocusSessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FocusSessionEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final Integer _tmpLinkedTaskId;
            if (_cursor.isNull(_cursorIndexOfLinkedTaskId)) {
              _tmpLinkedTaskId = null;
            } else {
              _tmpLinkedTaskId = _cursor.getInt(_cursorIndexOfLinkedTaskId);
            }
            final int _tmpDurationMinutes;
            _tmpDurationMinutes = _cursor.getInt(_cursorIndexOfDurationMinutes);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final Integer _tmpEfficiencyPercent;
            if (_cursor.isNull(_cursorIndexOfEfficiencyPercent)) {
              _tmpEfficiencyPercent = null;
            } else {
              _tmpEfficiencyPercent = _cursor.getInt(_cursorIndexOfEfficiencyPercent);
            }
            final int _tmpBreakDurationMinutes;
            _tmpBreakDurationMinutes = _cursor.getInt(_cursorIndexOfBreakDurationMinutes);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final Long _tmpSyncedAt;
            if (_cursor.isNull(_cursorIndexOfSyncedAt)) {
              _tmpSyncedAt = null;
            } else {
              _tmpSyncedAt = _cursor.getLong(_cursorIndexOfSyncedAt);
            }
            _item = new FocusSessionEntity(_tmpId,_tmpLinkedTaskId,_tmpDurationMinutes,_tmpStartTime,_tmpEndTime,_tmpEfficiencyPercent,_tmpBreakDurationMinutes,_tmpRemoteId,_tmpSyncedAt);
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

  @Override
  public Flow<Integer> getTodayFocusMinutes(final long startOfDay, final long endOfDay) {
    final String _sql = "SELECT COALESCE(SUM(duration_minutes), 0) FROM focus_sessions WHERE start_time >= ? AND start_time < ? AND end_time IS NOT NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfDay);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfDay);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"focus_sessions"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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

  @Override
  public Flow<List<FocusSessionEntity>> getAllSessions() {
    final String _sql = "SELECT * FROM focus_sessions ORDER BY start_time DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"focus_sessions"}, new Callable<List<FocusSessionEntity>>() {
      @Override
      @NonNull
      public List<FocusSessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLinkedTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "linked_task_id");
          final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_minutes");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_time");
          final int _cursorIndexOfEfficiencyPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "efficiency_percent");
          final int _cursorIndexOfBreakDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "break_duration_minutes");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "synced_at");
          final List<FocusSessionEntity> _result = new ArrayList<FocusSessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FocusSessionEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final Integer _tmpLinkedTaskId;
            if (_cursor.isNull(_cursorIndexOfLinkedTaskId)) {
              _tmpLinkedTaskId = null;
            } else {
              _tmpLinkedTaskId = _cursor.getInt(_cursorIndexOfLinkedTaskId);
            }
            final int _tmpDurationMinutes;
            _tmpDurationMinutes = _cursor.getInt(_cursorIndexOfDurationMinutes);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final Integer _tmpEfficiencyPercent;
            if (_cursor.isNull(_cursorIndexOfEfficiencyPercent)) {
              _tmpEfficiencyPercent = null;
            } else {
              _tmpEfficiencyPercent = _cursor.getInt(_cursorIndexOfEfficiencyPercent);
            }
            final int _tmpBreakDurationMinutes;
            _tmpBreakDurationMinutes = _cursor.getInt(_cursorIndexOfBreakDurationMinutes);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final Long _tmpSyncedAt;
            if (_cursor.isNull(_cursorIndexOfSyncedAt)) {
              _tmpSyncedAt = null;
            } else {
              _tmpSyncedAt = _cursor.getLong(_cursorIndexOfSyncedAt);
            }
            _item = new FocusSessionEntity(_tmpId,_tmpLinkedTaskId,_tmpDurationMinutes,_tmpStartTime,_tmpEndTime,_tmpEfficiencyPercent,_tmpBreakDurationMinutes,_tmpRemoteId,_tmpSyncedAt);
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

  @Override
  public Flow<List<FocusSessionEntity>> getSessionsForTask(final int taskId) {
    final String _sql = "SELECT * FROM focus_sessions WHERE linked_task_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, taskId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"focus_sessions"}, new Callable<List<FocusSessionEntity>>() {
      @Override
      @NonNull
      public List<FocusSessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLinkedTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "linked_task_id");
          final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_minutes");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_time");
          final int _cursorIndexOfEfficiencyPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "efficiency_percent");
          final int _cursorIndexOfBreakDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "break_duration_minutes");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "synced_at");
          final List<FocusSessionEntity> _result = new ArrayList<FocusSessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FocusSessionEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final Integer _tmpLinkedTaskId;
            if (_cursor.isNull(_cursorIndexOfLinkedTaskId)) {
              _tmpLinkedTaskId = null;
            } else {
              _tmpLinkedTaskId = _cursor.getInt(_cursorIndexOfLinkedTaskId);
            }
            final int _tmpDurationMinutes;
            _tmpDurationMinutes = _cursor.getInt(_cursorIndexOfDurationMinutes);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final Integer _tmpEfficiencyPercent;
            if (_cursor.isNull(_cursorIndexOfEfficiencyPercent)) {
              _tmpEfficiencyPercent = null;
            } else {
              _tmpEfficiencyPercent = _cursor.getInt(_cursorIndexOfEfficiencyPercent);
            }
            final int _tmpBreakDurationMinutes;
            _tmpBreakDurationMinutes = _cursor.getInt(_cursorIndexOfBreakDurationMinutes);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final Long _tmpSyncedAt;
            if (_cursor.isNull(_cursorIndexOfSyncedAt)) {
              _tmpSyncedAt = null;
            } else {
              _tmpSyncedAt = _cursor.getLong(_cursorIndexOfSyncedAt);
            }
            _item = new FocusSessionEntity(_tmpId,_tmpLinkedTaskId,_tmpDurationMinutes,_tmpStartTime,_tmpEndTime,_tmpEfficiencyPercent,_tmpBreakDurationMinutes,_tmpRemoteId,_tmpSyncedAt);
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

  @Override
  public Flow<List<FocusSessionEntity>> getSessionsThisWeek(final long weekStart) {
    final String _sql = "SELECT * FROM focus_sessions WHERE start_time >= ? AND end_time IS NOT NULL ORDER BY start_time ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, weekStart);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"focus_sessions"}, new Callable<List<FocusSessionEntity>>() {
      @Override
      @NonNull
      public List<FocusSessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLinkedTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "linked_task_id");
          final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_minutes");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_time");
          final int _cursorIndexOfEfficiencyPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "efficiency_percent");
          final int _cursorIndexOfBreakDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "break_duration_minutes");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "synced_at");
          final List<FocusSessionEntity> _result = new ArrayList<FocusSessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FocusSessionEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final Integer _tmpLinkedTaskId;
            if (_cursor.isNull(_cursorIndexOfLinkedTaskId)) {
              _tmpLinkedTaskId = null;
            } else {
              _tmpLinkedTaskId = _cursor.getInt(_cursorIndexOfLinkedTaskId);
            }
            final int _tmpDurationMinutes;
            _tmpDurationMinutes = _cursor.getInt(_cursorIndexOfDurationMinutes);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final Integer _tmpEfficiencyPercent;
            if (_cursor.isNull(_cursorIndexOfEfficiencyPercent)) {
              _tmpEfficiencyPercent = null;
            } else {
              _tmpEfficiencyPercent = _cursor.getInt(_cursorIndexOfEfficiencyPercent);
            }
            final int _tmpBreakDurationMinutes;
            _tmpBreakDurationMinutes = _cursor.getInt(_cursorIndexOfBreakDurationMinutes);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final Long _tmpSyncedAt;
            if (_cursor.isNull(_cursorIndexOfSyncedAt)) {
              _tmpSyncedAt = null;
            } else {
              _tmpSyncedAt = _cursor.getLong(_cursorIndexOfSyncedAt);
            }
            _item = new FocusSessionEntity(_tmpId,_tmpLinkedTaskId,_tmpDurationMinutes,_tmpStartTime,_tmpEndTime,_tmpEfficiencyPercent,_tmpBreakDurationMinutes,_tmpRemoteId,_tmpSyncedAt);
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

  @Override
  public Object getActiveSession(final Continuation<? super FocusSessionEntity> $completion) {
    final String _sql = "SELECT * FROM focus_sessions WHERE end_time IS NULL LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<FocusSessionEntity>() {
      @Override
      @Nullable
      public FocusSessionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLinkedTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "linked_task_id");
          final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_minutes");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_time");
          final int _cursorIndexOfEfficiencyPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "efficiency_percent");
          final int _cursorIndexOfBreakDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "break_duration_minutes");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "synced_at");
          final FocusSessionEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final Integer _tmpLinkedTaskId;
            if (_cursor.isNull(_cursorIndexOfLinkedTaskId)) {
              _tmpLinkedTaskId = null;
            } else {
              _tmpLinkedTaskId = _cursor.getInt(_cursorIndexOfLinkedTaskId);
            }
            final int _tmpDurationMinutes;
            _tmpDurationMinutes = _cursor.getInt(_cursorIndexOfDurationMinutes);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final Integer _tmpEfficiencyPercent;
            if (_cursor.isNull(_cursorIndexOfEfficiencyPercent)) {
              _tmpEfficiencyPercent = null;
            } else {
              _tmpEfficiencyPercent = _cursor.getInt(_cursorIndexOfEfficiencyPercent);
            }
            final int _tmpBreakDurationMinutes;
            _tmpBreakDurationMinutes = _cursor.getInt(_cursorIndexOfBreakDurationMinutes);
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final Long _tmpSyncedAt;
            if (_cursor.isNull(_cursorIndexOfSyncedAt)) {
              _tmpSyncedAt = null;
            } else {
              _tmpSyncedAt = _cursor.getLong(_cursorIndexOfSyncedAt);
            }
            _result = new FocusSessionEntity(_tmpId,_tmpLinkedTaskId,_tmpDurationMinutes,_tmpStartTime,_tmpEndTime,_tmpEfficiencyPercent,_tmpBreakDurationMinutes,_tmpRemoteId,_tmpSyncedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Integer> getTotalFocusMinutes() {
    final String _sql = "SELECT COALESCE(SUM(duration_minutes), 0) FROM focus_sessions WHERE end_time IS NOT NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"focus_sessions"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
