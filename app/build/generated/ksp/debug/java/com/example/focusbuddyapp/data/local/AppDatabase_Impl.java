package com.example.focusbuddyapp.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.focusbuddyapp.data.local.dao.FocusSessionDao;
import com.example.focusbuddyapp.data.local.dao.FocusSessionDao_Impl;
import com.example.focusbuddyapp.data.local.dao.SubTaskDao;
import com.example.focusbuddyapp.data.local.dao.SubTaskDao_Impl;
import com.example.focusbuddyapp.data.local.dao.TaskDao;
import com.example.focusbuddyapp.data.local.dao.TaskDao_Impl;
import com.example.focusbuddyapp.data.local.dao.UserDao;
import com.example.focusbuddyapp.data.local.dao.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UserDao _userDao;

  private volatile TaskDao _taskDao;

  private volatile SubTaskDao _subTaskDao;

  private volatile FocusSessionDao _focusSessionDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `email` TEXT NOT NULL, `major` TEXT NOT NULL, `avatar_url` TEXT NOT NULL, `password` TEXT NOT NULL, `created_at` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tasks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `category` TEXT NOT NULL, `priority` TEXT NOT NULL, `due_date` INTEGER, `due_time` TEXT, `is_completed` INTEGER NOT NULL, `progress_percent` INTEGER NOT NULL, `study_notes` TEXT NOT NULL, `remote_id` TEXT, `synced_at` INTEGER, `user_id` INTEGER NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_user_id` ON `tasks` (`user_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_priority` ON `tasks` (`priority`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_is_completed` ON `tasks` (`is_completed`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `subtasks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `parent_task_id` INTEGER NOT NULL, `title` TEXT NOT NULL, `is_completed` INTEGER NOT NULL, `is_urgent` INTEGER NOT NULL, FOREIGN KEY(`parent_task_id`) REFERENCES `tasks`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_subtasks_parent_task_id` ON `subtasks` (`parent_task_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `focus_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `linked_task_id` INTEGER, `duration_minutes` INTEGER NOT NULL, `start_time` INTEGER NOT NULL, `end_time` INTEGER, `efficiency_percent` INTEGER, `break_duration_minutes` INTEGER NOT NULL, `remote_id` TEXT, `synced_at` INTEGER, FOREIGN KEY(`linked_task_id`) REFERENCES `tasks`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_focus_sessions_linked_task_id` ON `focus_sessions` (`linked_task_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3811e0eeb3e16d0656bbc99efa59633f')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `tasks`");
        db.execSQL("DROP TABLE IF EXISTS `subtasks`");
        db.execSQL("DROP TABLE IF EXISTS `focus_sessions`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(7);
        _columnsUsers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("major", new TableInfo.Column("major", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("avatar_url", new TableInfo.Column("avatar_url", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("password", new TableInfo.Column("password", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.example.focusbuddyapp.data.local.entity.UserEntity).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsTasks = new HashMap<String, TableInfo.Column>(13);
        _columnsTasks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("priority", new TableInfo.Column("priority", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("due_date", new TableInfo.Column("due_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("due_time", new TableInfo.Column("due_time", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("is_completed", new TableInfo.Column("is_completed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("progress_percent", new TableInfo.Column("progress_percent", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("study_notes", new TableInfo.Column("study_notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("remote_id", new TableInfo.Column("remote_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("synced_at", new TableInfo.Column("synced_at", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("user_id", new TableInfo.Column("user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTasks = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTasks.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("user_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTasks = new HashSet<TableInfo.Index>(3);
        _indicesTasks.add(new TableInfo.Index("index_tasks_user_id", false, Arrays.asList("user_id"), Arrays.asList("ASC")));
        _indicesTasks.add(new TableInfo.Index("index_tasks_priority", false, Arrays.asList("priority"), Arrays.asList("ASC")));
        _indicesTasks.add(new TableInfo.Index("index_tasks_is_completed", false, Arrays.asList("is_completed"), Arrays.asList("ASC")));
        final TableInfo _infoTasks = new TableInfo("tasks", _columnsTasks, _foreignKeysTasks, _indicesTasks);
        final TableInfo _existingTasks = TableInfo.read(db, "tasks");
        if (!_infoTasks.equals(_existingTasks)) {
          return new RoomOpenHelper.ValidationResult(false, "tasks(com.example.focusbuddyapp.data.local.entity.TaskEntity).\n"
                  + " Expected:\n" + _infoTasks + "\n"
                  + " Found:\n" + _existingTasks);
        }
        final HashMap<String, TableInfo.Column> _columnsSubtasks = new HashMap<String, TableInfo.Column>(5);
        _columnsSubtasks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubtasks.put("parent_task_id", new TableInfo.Column("parent_task_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubtasks.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubtasks.put("is_completed", new TableInfo.Column("is_completed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubtasks.put("is_urgent", new TableInfo.Column("is_urgent", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSubtasks = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSubtasks.add(new TableInfo.ForeignKey("tasks", "CASCADE", "NO ACTION", Arrays.asList("parent_task_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesSubtasks = new HashSet<TableInfo.Index>(1);
        _indicesSubtasks.add(new TableInfo.Index("index_subtasks_parent_task_id", false, Arrays.asList("parent_task_id"), Arrays.asList("ASC")));
        final TableInfo _infoSubtasks = new TableInfo("subtasks", _columnsSubtasks, _foreignKeysSubtasks, _indicesSubtasks);
        final TableInfo _existingSubtasks = TableInfo.read(db, "subtasks");
        if (!_infoSubtasks.equals(_existingSubtasks)) {
          return new RoomOpenHelper.ValidationResult(false, "subtasks(com.example.focusbuddyapp.data.local.entity.SubTaskEntity).\n"
                  + " Expected:\n" + _infoSubtasks + "\n"
                  + " Found:\n" + _existingSubtasks);
        }
        final HashMap<String, TableInfo.Column> _columnsFocusSessions = new HashMap<String, TableInfo.Column>(9);
        _columnsFocusSessions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusSessions.put("linked_task_id", new TableInfo.Column("linked_task_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusSessions.put("duration_minutes", new TableInfo.Column("duration_minutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusSessions.put("start_time", new TableInfo.Column("start_time", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusSessions.put("end_time", new TableInfo.Column("end_time", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusSessions.put("efficiency_percent", new TableInfo.Column("efficiency_percent", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusSessions.put("break_duration_minutes", new TableInfo.Column("break_duration_minutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusSessions.put("remote_id", new TableInfo.Column("remote_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusSessions.put("synced_at", new TableInfo.Column("synced_at", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFocusSessions = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysFocusSessions.add(new TableInfo.ForeignKey("tasks", "SET NULL", "NO ACTION", Arrays.asList("linked_task_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesFocusSessions = new HashSet<TableInfo.Index>(1);
        _indicesFocusSessions.add(new TableInfo.Index("index_focus_sessions_linked_task_id", false, Arrays.asList("linked_task_id"), Arrays.asList("ASC")));
        final TableInfo _infoFocusSessions = new TableInfo("focus_sessions", _columnsFocusSessions, _foreignKeysFocusSessions, _indicesFocusSessions);
        final TableInfo _existingFocusSessions = TableInfo.read(db, "focus_sessions");
        if (!_infoFocusSessions.equals(_existingFocusSessions)) {
          return new RoomOpenHelper.ValidationResult(false, "focus_sessions(com.example.focusbuddyapp.data.local.entity.FocusSessionEntity).\n"
                  + " Expected:\n" + _infoFocusSessions + "\n"
                  + " Found:\n" + _existingFocusSessions);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3811e0eeb3e16d0656bbc99efa59633f", "b63a54e6d13327ebbc78a53de3bdb863");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users","tasks","subtasks","focus_sessions");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `tasks`");
      _db.execSQL("DELETE FROM `subtasks`");
      _db.execSQL("DELETE FROM `focus_sessions`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TaskDao.class, TaskDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SubTaskDao.class, SubTaskDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FocusSessionDao.class, FocusSessionDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public TaskDao taskDao() {
    if (_taskDao != null) {
      return _taskDao;
    } else {
      synchronized(this) {
        if(_taskDao == null) {
          _taskDao = new TaskDao_Impl(this);
        }
        return _taskDao;
      }
    }
  }

  @Override
  public SubTaskDao subTaskDao() {
    if (_subTaskDao != null) {
      return _subTaskDao;
    } else {
      synchronized(this) {
        if(_subTaskDao == null) {
          _subTaskDao = new SubTaskDao_Impl(this);
        }
        return _subTaskDao;
      }
    }
  }

  @Override
  public FocusSessionDao focusSessionDao() {
    if (_focusSessionDao != null) {
      return _focusSessionDao;
    } else {
      synchronized(this) {
        if(_focusSessionDao == null) {
          _focusSessionDao = new FocusSessionDao_Impl(this);
        }
        return _focusSessionDao;
      }
    }
  }
}
