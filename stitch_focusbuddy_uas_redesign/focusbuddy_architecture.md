# FocusBuddy — Complete Android Architecture Plan (UAS)

> **Sumber Kebenaran:** README.md (Design System) · PRD Kelompok 3 · Stitch UI Mockups  
> **Namespace:** `com.example.focusbuddyapp`  
> **Min SDK:** 24 · **Target SDK:** 36 · **Language:** Kotlin · **UI:** Jetpack Compose

---

## 1. Architectural Pattern

```
MVVM  +  Clean Architecture  +  Repository Pattern  +  Offline-First
```

### Layer Stack (Bottom → Top)

| Layer | Responsibility | Academic Requirement Fulfilled |
|-------|---------------|-------------------------------|
| **Data** | Room, Retrofit, DataStore, Repositories | Room DB, Retrofit API, Repository Pattern |
| **Domain** | Entities, Use Cases, Repository Interfaces | Clean Architecture, Use Cases |
| **Presentation** | ViewModels, UI State, Screens | MVVM, StateFlow, State Preservation |
| **UI (Compose)** | Screens, Components, Navigation | Navigation Compose, LazyColumn |

---

## 2. Full Folder & Package Structure

```
app/src/main/java/com/example/focusbuddyapp/
│
├── di/                                   ← Dependency injection modules (manual DI)
│   ├── AppModule.kt                      ← Database + Retrofit + Repository bindings
│   └── NetworkModule.kt                  ← Retrofit/OkHttp setup
│
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt                ← Room database entrypoint
│   │   ├── dao/
│   │   │   ├── TaskDao.kt
│   │   │   ├── SubTaskDao.kt
│   │   │   ├── FocusSessionDao.kt
│   │   │   └── UserDao.kt
│   │   └── entity/
│   │       ├── TaskEntity.kt
│   │       ├── SubTaskEntity.kt
│   │       ├── FocusSessionEntity.kt
│   │       └── UserEntity.kt
│   │
│   ├── remote/
│   │   ├── api/
│   │   │   ├── AuthApiService.kt         ← Login / Register endpoints
│   │   │   ├── TaskApiService.kt         ← Remote task CRUD
│   │   │   ├── FocusSessionApiService.kt ← Remote session sync
│   │   │   └── QuoteApiService.kt        ← Motivational quote (public API)
│   │   ├── dto/
│   │   │   ├── LoginRequestDto.kt
│   │   │   ├── LoginResponseDto.kt
│   │   │   ├── RegisterRequestDto.kt
│   │   │   ├── TaskDto.kt
│   │   │   ├── SubTaskDto.kt
│   │   │   ├── FocusSessionDto.kt
│   │   │   └── QuoteDto.kt
│   │   └── interceptor/
│   │       └── AuthInterceptor.kt        ← Attach Bearer token to requests
│   │
│   ├── repository/
│   │   ├── AuthRepositoryImpl.kt
│   │   ├── TaskRepositoryImpl.kt
│   │   ├── FocusSessionRepositoryImpl.kt
│   │   └── QuoteRepositoryImpl.kt
│   │
│   └── preferences/
│       └── UserPreferences.kt            ← DataStore: auth token, user prefs
│
├── domain/
│   ├── model/
│   │   ├── User.kt                       ← Domain model (clean, no Android deps)
│   │   ├── Task.kt
│   │   ├── SubTask.kt
│   │   ├── FocusSession.kt
│   │   └── Quote.kt
│   │
│   ├── repository/
│   │   ├── AuthRepository.kt             ← Interface
│   │   ├── TaskRepository.kt             ← Interface
│   │   ├── FocusSessionRepository.kt     ← Interface
│   │   └── QuoteRepository.kt            ← Interface
│   │
│   └── usecase/
│       ├── auth/
│       │   ├── LoginUseCase.kt
│       │   ├── RegisterUseCase.kt
│       │   └── LogoutUseCase.kt
│       ├── task/
│       │   ├── BrowseTasksUseCase.kt     ← BREAD: Browse
│       │   ├── ReadTaskUseCase.kt        ← BREAD: Read
│       │   ├── AddTaskUseCase.kt         ← BREAD: Add
│       │   ├── EditTaskUseCase.kt        ← BREAD: Edit
│       │   ├── DeleteTaskUseCase.kt      ← BREAD: Delete
│       │   ├── SearchTasksUseCase.kt
│       │   ├── FilterTasksUseCase.kt
│       │   └── ToggleTaskCompleteUseCase.kt
│       ├── subtask/
│       │   ├── AddSubTaskUseCase.kt
│       │   ├── ToggleSubTaskUseCase.kt
│       │   └── DeleteSubTaskUseCase.kt
│       ├── focus/
│       │   ├── StartFocusSessionUseCase.kt
│       │   ├── PauseFocusSessionUseCase.kt
│       │   ├── StopFocusSessionUseCase.kt
│       │   └── GetTodayFocusSummaryUseCase.kt
│       └── analytics/
│           ├── GetWeeklyFocusDataUseCase.kt
│           ├── GetProductivityInsightUseCase.kt
│           └── GetStreakDataUseCase.kt
│
├── presentation/
│   ├── navigation/
│   │   ├── NavGraph.kt                   ← NavHost + all composable routes
│   │   ├── Screen.kt                     ← Sealed class for route strings
│   │   └── BottomNavItem.kt              ← Bottom nav item definitions
│   │
│   ├── splash/
│   │   ├── SplashScreen.kt
│   │   └── SplashViewModel.kt
│   │
│   ├── auth/
│   │   ├── login/
│   │   │   ├── LoginScreen.kt
│   │   │   ├── LoginViewModel.kt
│   │   │   └── LoginUiState.kt
│   │   └── register/
│   │       ├── RegisterScreen.kt
│   │       ├── RegisterViewModel.kt
│   │       └── RegisterUiState.kt
│   │
│   ├── dashboard/
│   │   ├── DashboardScreen.kt
│   │   ├── DashboardViewModel.kt
│   │   └── DashboardUiState.kt
│   │
│   ├── task/
│   │   ├── list/
│   │   │   ├── TaskListScreen.kt         ← LazyColumn lives here
│   │   │   ├── TaskListViewModel.kt
│   │   │   └── TaskListUiState.kt
│   │   ├── detail/
│   │   │   ├── TaskDetailScreen.kt
│   │   │   ├── TaskDetailViewModel.kt
│   │   │   └── TaskDetailUiState.kt
│   │   └── addedit/
│   │       ├── AddEditTaskScreen.kt
│   │       ├── AddEditTaskViewModel.kt
│   │       └── AddEditTaskUiState.kt
│   │
│   ├── focus/
│   │   ├── FocusScreen.kt
│   │   ├── FocusViewModel.kt
│   │   └── FocusUiState.kt
│   │
│   ├── progress/
│   │   ├── ProgressScreen.kt
│   │   ├── ProgressViewModel.kt
│   │   └── ProgressUiState.kt
│   │
│   └── profile/
│       ├── ProfileScreen.kt
│       ├── ProfileViewModel.kt
│       └── ProfileUiState.kt
│
├── ui/
│   ├── theme/
│   │   ├── Color.kt                      ← Design system colors (Navy, Terracotta, etc.)
│   │   ├── Type.kt                       ← Manrope / Hanken Grotesk / JetBrains Mono
│   │   ├── Shape.kt                      ← 8px / 12px / 24px rounding strategy
│   │   └── Theme.kt                      ← MaterialTheme binding
│   └── components/
│       ├── FocusBuddyBottomNav.kt
│       ├── TaskCard.kt                   ← Reusable card with priority chip
│       ├── PriorityChip.kt               ← HIGH / MEDIUM / LOW chip
│       ├── CircularTimer.kt              ← Canvas-drawn arc timer
│       ├── StatCard.kt
│       ├── SectionHeader.kt
│       └── LoadingIndicator.kt
│
└── MainActivity.kt                       ← Single Activity, hosts NavHost
```

---

## 3. Layer Explanation

### 3.1 Data Layer
Responsible for all data operations. Completely hidden from Presentation.

- **Room (Local):** Source of truth for offline-first support. All data is first written to Room, then synced to remote.
- **Retrofit (Remote):** Fetches and pushes data to backend API. Results are mapped to DTOs → Domain models → Entities before storage.
- **DataStore Preferences:** Stores auth token, user display name, timer preferences (Pomodoro duration, break duration), notification toggles.
- **Repository Implementations:** Each `*RepositoryImpl` combines local + remote data using coroutines. Returns `Flow<>` so UI can react to changes.

### 3.2 Domain Layer
Pure Kotlin — zero Android framework dependencies.

- **Domain Models:** Clean data classes (`Task`, `User`, `FocusSession`, etc.) used across the app.
- **Repository Interfaces:** Contracts that Domain defines; Data implements.
- **Use Cases:** Single-responsibility classes. Each exposes one `operator fun invoke()`. ViewModels call Use Cases, never repositories directly.

### 3.3 Presentation Layer
One ViewModel per Screen. Exposes `StateFlow<UiState>`.

- ViewModels survive configuration changes (`viewModel()` scoped to NavBackStackEntry).
- Each screen has a dedicated `*UiState` sealed class covering `Loading`, `Success`, and `Error` states.
- `LazyColumn` in `TaskListScreen` observes the task list from StateFlow.

### 3.4 UI / Compose Layer
Stateless composables that receive state and emit events.

- All screens are `@Composable` functions receiving ViewModel state.
- Navigation handled by `NavGraph.kt` with `NavController`.

---

## 4. Entity List

| Entity | Room Table | Key Fields |
|--------|-----------|------------|
| `UserEntity` | `users` | id, name, email, major, avatarUrl, createdAt |
| `TaskEntity` | `tasks` | id, title, description, category, priority, dueDate, dueTime, isCompleted, progressPercent, remoteId, syncedAt, userId |
| `SubTaskEntity` | `subtasks` | id, parentTaskId, title, isCompleted, isUrgent |
| `FocusSessionEntity` | `focus_sessions` | id, linkedTaskId, durationMinutes, startTime, endTime, efficiencyPercent, breakDurationMinutes, remoteId, syncedAt |

---

## 5. Room Schema

```
Database: focusbuddy.db  (version 1)

TABLE users
  id            INTEGER PRIMARY KEY AUTOINCREMENT
  name          TEXT NOT NULL
  email         TEXT NOT NULL UNIQUE
  major         TEXT
  avatar_url    TEXT
  created_at    INTEGER  ← epoch millis

TABLE tasks
  id              INTEGER PRIMARY KEY AUTOINCREMENT
  title           TEXT NOT NULL
  description     TEXT
  category        TEXT NOT NULL             ← e.g. "Academic Focus", "Research"
  priority        TEXT NOT NULL             ← "HIGH" | "MEDIUM" | "LOW"
  due_date        INTEGER                   ← epoch millis (nullable)
  due_time        TEXT                      ← "HH:mm" string (nullable)
  is_completed    INTEGER NOT NULL DEFAULT 0
  progress_percent INTEGER NOT NULL DEFAULT 0
  study_notes     TEXT
  remote_id       TEXT                      ← server UUID (nullable, for sync)
  synced_at       INTEGER                   ← last sync epoch (nullable)
  user_id         INTEGER NOT NULL
  FOREIGN KEY (user_id) REFERENCES users(id)

TABLE subtasks
  id              INTEGER PRIMARY KEY AUTOINCREMENT
  parent_task_id  INTEGER NOT NULL
  title           TEXT NOT NULL
  is_completed    INTEGER NOT NULL DEFAULT 0
  is_urgent       INTEGER NOT NULL DEFAULT 0
  FOREIGN KEY (parent_task_id) REFERENCES tasks(id) ON DELETE CASCADE

TABLE focus_sessions
  id                    INTEGER PRIMARY KEY AUTOINCREMENT
  linked_task_id        INTEGER             ← nullable FK to tasks
  duration_minutes      INTEGER NOT NULL
  start_time            INTEGER NOT NULL    ← epoch millis
  end_time              INTEGER             ← epoch millis (nullable, if stopped early)
  efficiency_percent    INTEGER
  break_duration_minutes INTEGER NOT NULL DEFAULT 5
  remote_id             TEXT
  synced_at             INTEGER
  FOREIGN KEY (linked_task_id) REFERENCES tasks(id) ON DELETE SET NULL
```

**Indexes:**
- `tasks(user_id)` — faster per-user queries
- `tasks(priority)` — for filter operations
- `tasks(is_completed)` — for dashboard "completed today"
- `subtasks(parent_task_id)` — for joins in TaskDetail
- `focus_sessions(linked_task_id)` — for progress analytics

---

## 6. Repository Design

### `TaskRepository` (Interface in Domain)
```
interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>           // Browse — reactive stream
    suspend fun getTaskById(id: Int): Task?       // Read
    suspend fun addTask(task: Task): Long         // Add
    suspend fun updateTask(task: Task)            // Edit
    suspend fun deleteTask(id: Int)               // Delete
    suspend fun toggleTaskComplete(id: Int, done: Boolean)
    fun searchTasks(query: String): Flow<List<Task>>
    fun getTasksByPriority(priority: String): Flow<List<Task>>
    suspend fun syncWithRemote()                  // push local → server
}
```

### `FocusSessionRepository` (Interface in Domain)
```
interface FocusSessionRepository {
    suspend fun startSession(session: FocusSession): Long
    suspend fun updateSession(session: FocusSession)
    fun getTodaySessions(): Flow<List<FocusSession>>
    fun getWeeklyFocusMinutes(): Flow<Map<DayOfWeek, Int>>
    suspend fun syncWithRemote()
}
```

### `AuthRepository` (Interface in Domain)
```
interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(name: String, email: String, password: String): Result<User>
    suspend fun logout()
    fun getCurrentUser(): Flow<User?>
    fun isLoggedIn(): Boolean
}
```

### `QuoteRepository` (Interface in Domain)
```
interface QuoteRepository {
    suspend fun getDailyQuote(): Quote      // fetches from remote, fallbacks to hardcoded
}
```

### Offline-First Strategy for `TaskRepositoryImpl`
```
getAllTasks():
  1. Immediately emit data from Room (offline cache)
  2. Launch coroutine to fetch from Retrofit
  3. On success: upsert to Room → Room emits new data automatically
  4. On failure: keep emitting cached Room data (offline-first achieved)
```

---

## 7. Use Case Design

Each Use Case is a single `class` with `operator fun invoke(...)`.

### BREAD Mapping (Task)

| BREAD | Use Case | Trigger Screen |
|-------|----------|---------------|
| **B**rowse | `BrowseTasksUseCase` | TaskListScreen, DashboardScreen |
| **R**ead | `ReadTaskUseCase(id)` | TaskDetailScreen |
| **A**dd | `AddTaskUseCase(task)` | AddEditTaskScreen (new) |
| **E**dit | `EditTaskUseCase(task)` | AddEditTaskScreen (edit mode) |
| **D**elete | `DeleteTaskUseCase(id)` | TaskDetailScreen (delete button) |

### Focus Session Use Cases
- `StartFocusSessionUseCase` — creates FocusSession record, returns session ID
- `PauseFocusSessionUseCase(sessionId)` — saves elapsed time
- `StopFocusSessionUseCase(sessionId)` — finalizes session, calculates efficiency
- `GetTodayFocusSummaryUseCase` — aggregates today's minutes + efficiency

### Analytics Use Cases
- `GetWeeklyFocusDataUseCase` — returns `Map<DayOfWeek, Int>` (minutes per day) for bar chart
- `GetProductivityInsightUseCase` — returns insight text + best day of week
- `GetStreakDataUseCase` — returns current streak count

---

## 8. API Integration Plan

### Base URL Strategy
```
Production:  https://api.focusbuddy.app/v1/
Development: https://dev-api.focusbuddy.app/v1/   (or mock server)
Quote API:   https://api.quotable.io/random        (free, no auth required)
```

### Auth API (`AuthApiService.kt`)
```
POST /auth/login      → LoginRequestDto → LoginResponseDto (token + user)
POST /auth/register   → RegisterRequestDto → LoginResponseDto
POST /auth/logout     → (Bearer token required)
```

### Task API (`TaskApiService.kt`)
```
GET    /tasks              → List<TaskDto>      (Browse)
GET    /tasks/{id}         → TaskDto            (Read)
POST   /tasks              → TaskDto            (Add)
PUT    /tasks/{id}         → TaskDto            (Edit)
DELETE /tasks/{id}         → 204 No Content     (Delete)
PATCH  /tasks/{id}/complete → TaskDto           (toggle)
```

### Focus Session API (`FocusSessionApiService.kt`)
```
POST /sessions             → FocusSessionDto   (create)
PUT  /sessions/{id}        → FocusSessionDto   (update/finalize)
GET  /sessions/weekly      → List<FocusSessionDto>
```

### Quote API (`QuoteApiService.kt`)
```
GET https://api.quotable.io/random?tags=inspirational,study
→ QuoteDto { content, author }
```

### Retrofit Configuration
- `OkHttpClient` with `AuthInterceptor` (adds `Authorization: Bearer <token>`)
- `GsonConverterFactory` for JSON parsing
- Timeout: connect=10s, read=30s, write=30s
- Retry logic: 1 automatic retry on `IOException`

### DTO → Domain Mapper pattern
Each DTO has a `.toDomain()` extension function.  
Each Domain model has a `.toEntity()` extension function.  
Each Entity has a `.toDomain()` extension function.

---

## 9. Navigation Graph

### Route Sealed Class (`Screen.kt`)

```kotlin
sealed class Screen(val route: String) {
    object Splash        : Screen("splash")
    object Login         : Screen("login")
    object Register      : Screen("register")
    object Dashboard     : Screen("dashboard")
    object TaskList      : Screen("task_list")
    object TaskDetail    : Screen("task_detail/{taskId}") {
        fun createRoute(id: Int) = "task_detail/$id"
    }
    object AddEditTask   : Screen("add_edit_task?taskId={taskId}") {
        fun createRoute(id: Int? = null) = if (id != null) "add_edit_task?taskId=$id" else "add_edit_task"
    }
    object FocusTimer    : Screen("focus_timer")
    object Progress      : Screen("progress")
    object Profile       : Screen("profile")
}
```

### Navigation Flow Diagram

```
[Splash] 
   │ (auto-navigate after init: check auth token)
   ├─► [Login] ──► [Register]
   │       │
   │       ▼ (on success)
   └─────► [MainScaffold] ← Bottom Navigation
               │
               ├─► Dashboard
               │      └── FAB (+) ──► AddEditTask (new)
               │
               ├─► TaskList (LazyColumn)
               │      ├── Task Card tap ──► TaskDetail
               │      │                        ├── Edit button ──► AddEditTask (edit mode, taskId)
               │      │                        └── Delete button ──► (pop back to TaskList)
               │      └── FAB (+) ──► AddEditTask (new)
               │
               ├─► FocusTimer
               │
               ├─► Progress
               │
               └─► Profile ──► (Logout → Login)
```

### NavGraph Key Rules
- `startDestination = Screen.Splash.route`
- After auth check in `SplashViewModel`:
  - If token exists → navigate to `Dashboard`, clear back stack to Splash
  - If no token → navigate to `Login`
- After Login/Register success → `Dashboard`, `popUpTo(Screen.Login) { inclusive = true }`
- After Logout → `Login`, `popUpTo(Screen.Dashboard) { inclusive = true }`
- `TaskDetail` and `AddEditTask` are **not** bottom nav destinations — they are overlays pushed on top.

### Bottom Navigation Items

| Label | Icon | Route |
|-------|------|-------|
| Dashboard | `Icons.Filled.Dashboard` | `Screen.Dashboard` |
| Tasks | `Icons.Filled.Assignment` | `Screen.TaskList` |
| Timer | `Icons.Filled.Timer` | `Screen.FocusTimer` |
| Progress | `Icons.Filled.BarChart` | `Screen.Progress` |
| Profile | `Icons.Filled.Person` | `Screen.Profile` |

Active color: Terracotta `#C4553D` · Inactive color: Muted Navy

---

## 10. State Management Strategy

### Per-Screen UiState Pattern

Every screen uses a **sealed interface** for UI state:

```
sealed interface TaskListUiState {
    object Loading : TaskListUiState
    data class Success(
        val tasks: List<Task>,
        val filteredTasks: List<Task>,
        val searchQuery: String,
        val selectedFilter: String,
        val isRefreshing: Boolean
    ) : TaskListUiState
    data class Error(val message: String) : TaskListUiState
}
```

### ViewModel StateFlow Pattern

```
class TaskListViewModel(
    private val browseTasksUseCase: BrowseTasksUseCase,
    private val searchTasksUseCase: SearchTasksUseCase,
    ...
) : ViewModel() {

    private val _uiState = MutableStateFlow<TaskListUiState>(TaskListUiState.Loading)
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() = viewModelScope.launch {
        browseTasksUseCase().collect { tasks ->
            _uiState.value = TaskListUiState.Success(tasks, tasks, "", "ALL", false)
        }
    }

    fun onSearch(query: String) { /* filter locally */ }
    fun onFilterChange(filter: String) { /* filter locally */ }
}
```

### State Preservation Rules

| Scenario | Mechanism |
|----------|-----------|
| Screen rotation | `ViewModel` survives config change automatically |
| Process death (task list) | Room `Flow` re-emits on restart from cached data |
| Focus timer mid-session | Timer state persisted to `FocusSessionEntity` every 30s, `SavedStateHandle` holds elapsed seconds |
| Form input (Add/Edit Task) | `rememberSaveable` + `SavedStateHandle` in ViewModel for form fields |
| Auth token | DataStore Preferences (survives process death) |
| Navigation back stack | `NavController` manages back stack; `viewModel()` scoped to `NavBackStackEntry` |

### FocusTimer State (special case)
The timer needs to survive screen navigation while running:
- `FocusViewModel` is scoped to the **Activity** (not NavBackStackEntry) so it persists when navigating away
- Timer runs using `viewModelScope` coroutine with `delay(1000L)` loop
- Current elapsed time is persisted to Room every 30 seconds as a "checkpoint"
- On app restart: read last unfinished session from Room and resume

### Coroutine Scope Usage

| Location | Scope | Purpose |
|----------|-------|---------|
| ViewModel operations | `viewModelScope` | All data loading, DB ops |
| Repository sync | `viewModelScope` (via VM) | Remote sync triggered by VM |
| Timer countdown | `viewModelScope` (in FocusViewModel) | 1s tick loop |
| Room DAO queries | `Flow` (collected by VM) | Reactive DB reads |
| Retrofit calls | `suspend fun` inside `withContext(Dispatchers.IO)` | Network I/O |

---

## 11. BREAD Compliance Summary

| Operation | Screen | ViewModel Function | Use Case | Room DAO | Remote API |
|-----------|--------|-------------------|----------|----------|-----------|
| **Browse** | TaskListScreen (LazyColumn) | `loadTasks()` | `BrowseTasksUseCase` | `TaskDao.getAllTasks(): Flow` | `GET /tasks` |
| **Read** | TaskDetailScreen | `loadTask(id)` | `ReadTaskUseCase` | `TaskDao.getTaskById(id)` | `GET /tasks/{id}` |
| **Add** | AddEditTaskScreen | `saveTask()` | `AddTaskUseCase` | `TaskDao.insert()` | `POST /tasks` |
| **Edit** | AddEditTaskScreen | `saveTask()` | `EditTaskUseCase` | `TaskDao.update()` | `PUT /tasks/{id}` |
| **Delete** | TaskDetailScreen | `deleteTask(id)` | `DeleteTaskUseCase` | `TaskDao.delete()` | `DELETE /tasks/{id}` |

---

## 12. Academic Requirements Checklist

| Requirement | Implementation Location | Notes |
|-------------|------------------------|-------|
| ✅ MVVM | `presentation/*/ViewModel.kt` | ViewModel + UiState + Composable |
| ✅ Clean Architecture | `data/` / `domain/` / `presentation/` | Strict layer separation |
| ✅ Remote API Fetching | `data/remote/api/` + Retrofit | Auth + Tasks + Quote APIs |
| ✅ Local Database | `data/local/` + Room | 4 tables, offline-first |
| ✅ BREAD | Task feature (Browse/Read/Add/Edit/Delete) | Full CRUD via TaskDao |
| ✅ State Preservation | `SavedStateHandle` + `rememberSaveable` + DataStore | All scenarios covered |
| ✅ LazyColumn | `TaskListScreen.kt` + `DashboardScreen.kt` | Dynamic task list |
| ✅ Navigation Compose | `NavGraph.kt` + `Screen.kt` | 9 routes, bottom nav |
| ✅ Repository Pattern | `domain/repository/` interfaces + `data/repository/` impls | 4 repositories |
| ✅ Room Database | `AppDatabase.kt` + 4 DAOs + 4 Entities | `focusbuddy.db` |
| ✅ Retrofit API | `AuthApiService`, `TaskApiService`, etc. | OkHttp + GsonConverter |
| ✅ Coroutines | Every suspend fun + Flow | IO dispatcher for network |
| ✅ StateFlow | `MutableStateFlow<UiState>` in every ViewModel | `asStateFlow()` exposed |
| ✅ Offline-First | `TaskRepositoryImpl` emits Room first, then syncs | Room as single source of truth |

---

## 13. Key Dependencies to Add (build.gradle.kts)

```kotlin
// Room
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// Retrofit + OkHttp
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// DataStore
implementation("androidx.datastore:datastore-preferences:1.0.0")

// ViewModel + Lifecycle
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

// Navigation Compose (already present)
implementation("androidx.navigation:navigation-compose:2.7.7")

// Coil (avatar image loading)
implementation("io.coil-kt:coil-compose:2.5.0")

// Kotlin Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// Google Fonts (Manrope, Hanken Grotesk)
implementation("androidx.compose.ui:ui-text-google-fonts:1.5.4")
```

> **Note on kapt:** Add `id("kotlin-kapt")` to the plugins block in `app/build.gradle.kts` for Room annotation processing.

---

## 14. Design System Tokens (from README + Stitch DESIGN.md)

### Colors (`ui/theme/Color.kt`)
```
PrimaryNavy       = #1F365C  (branding, header, cards)
WarmBackground    = #F7F3EE  (app background)
PrimaryTerracotta = #C4553D  (CTA, active nav, progress)
DarkTerracotta    = #B84C35  (pressed/selected state)
SuccessGreen      = #4F7A58  (completion, momentum)
PrimaryText       = #162033  (headings, titles)
SecondaryText     = #6D7380  (body, subtitles)
SurfaceWhite      = #FFFFFF  (cards)
```

### Typography (`ui/theme/Type.kt`)
- **Manrope** — Headlines (page titles, section headers)
- **Hanken Grotesk** — Body text (descriptions, task names)
- **JetBrains Mono** — Timer, percentages, priority labels

### Shapes (`ui/theme/Shape.kt`)
- Chips / Inputs: `8.dp` rounded
- Buttons / List items: `12.dp` rounded
- Cards / Modals: `24.dp` rounded

---

*Architecture plan created based on: README.md Design System · PRD Kelompok 3 FocusBuddy · Stitch UAS Redesign Mockups*
