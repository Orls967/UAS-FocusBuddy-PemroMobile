# FocusBuddy

FocusBuddy adalah aplikasi Android untuk membantu mahasiswa mengurangi procrastination melalui manajemen tugas, focus timer, progress analytics, dan activity tracking. Aplikasi ini dirancang sebagai tools produktivitas akademik yang membantu pengguna merencanakan tugas, menjalankan sesi fokus, memantau progres belajar, dan menjaga konsistensi kebiasaan belajar.

## Overview

FocusBuddy dikembangkan untuk kebutuhan UAS Pemrograman Mobile dengan pendekatan **MVVM + Clean Architecture**. Aplikasi menerapkan penyimpanan lokal menggunakan **Room Database**, preferensi pengguna menggunakan **DataStore Preferences**, serta API layer menggunakan **Retrofit**.

Untuk kebutuhan demo UAS, FocusBuddy menerapkan pendekatan **offline-first**, sehingga fitur utama tetap berjalan menggunakan Room Database walaupun koneksi server tidak tersedia. API service tetap disiapkan dan terintegrasi dalam struktur arsitektur, tetapi aplikasi tidak bergantung penuh pada koneksi server.

## Tech Stack

- Kotlin
- Jetpack Compose
- MVVM
- Clean Architecture
- Room Database
- DataStore Preferences
- Retrofit
- OkHttp
- Coroutines
- StateFlow
- Coil
- Material 3
- Manual Dependency Injection melalui `AppModule`

> Catatan: struktur dependency injection pada project ini menggunakan `AppModule` sebagai manual DI/service locator. Project belum menggunakan Hilt secara langsung.

## Cara Menjalankan Aplikasi

1. Clone repository dari GitHub ke komputer lokal menggunakan tombol **Code** atau URL repository project FocusBuddy.

2. Buka project menggunakan **Android Studio**.

3. Tunggu proses **Gradle Sync** selesai.

4. Jalankan emulator Android atau hubungkan perangkat Android fisik.

5. Klik tombol **Run** di Android Studio.

6. Aplikasi akan berjalan dengan package:

   ```text
   com.example.focusbuddyapp
   ```

Minimum SDK yang digunakan:

```text
minSdk = 24
targetSdk = 35
```

## Akun Demo

Project menyediakan akun demo lokal untuk kebutuhan pengujian aplikasi:

```text
Email    : demo@focusbuddy.com
Password : password
```

Saat login menggunakan akun demo, aplikasi akan membuat demo data berupa task aktif, task selesai, dan riwayat focus session untuk menampilkan Dashboard dan Progress Analytics.

## Fitur Utama

- Splash Screen
- Login
- Register
- Dashboard
- Task List
- Task Detail
- Add Task
- Edit Task
- Delete Task
- Search Task
- Filter Priority
- Difficulty Level
- Focus Timer
- Break Timer
- Progress Analytics
- Profile & Settings
- Dark Mode
- Profile Photo
- Notification Session Complete
- Active Task
- Focus Lock
- Demo User
- Demo Data

## Screen Aplikasi

FocusBuddy memiliki beberapa screen utama:

- **Splash Screen**: halaman awal aplikasi sebelum masuk ke auth flow.
- **Login**: halaman masuk pengguna.
- **Register**: halaman pendaftaran pengguna baru.
- **Dashboard**: ringkasan aktivitas, task hari ini, focus minutes, dan quote.
- **Task List**: daftar task menggunakan `LazyColumn`, dilengkapi search dan filter priority.
- **Task Detail**: detail task, status, priority, difficulty, durasi fokus, serta aksi edit/delete.
- **Add/Edit Task**: form tambah dan ubah task.
- **Focus Timer**: timer fokus, break timer, active task, dan focus lock.
- **Progress**: analytics progres belajar mingguan dan total task selesai.
- **Profile & Settings**: pengaturan profile, photo, dark mode, dan logout.

## Arsitektur Aplikasi

Arsitektur yang digunakan:

```text
MVVM + Clean Architecture
```

Layer utama:

- **Presentation Layer**
- **Domain Layer**
- **Data Layer**

Alur data aplikasi:

```text
UI → ViewModel → Use Case → Repository → Room/API
```

Diagram sederhana:

```text
Jetpack Compose UI
        |
        v
ViewModel + StateFlow
        |
        v
Use Case
        |
        v
Repository Interface
        |
        v
Repository Implementation
   |                  |
   v                  v
Room Database     Retrofit API
        |
        v
DataStore Preferences
```

### Presentation Layer

Presentation Layer berisi UI, screen, navigation, dan ViewModel.

Lokasi:

```text
app/src/main/java/com/example/focusbuddyapp/presentation
```

Contoh file:

- `presentation/auth/login/LoginScreen.kt`
- `presentation/auth/register/RegisterScreen.kt`
- `presentation/dashboard/DashboardScreen.kt`
- `presentation/task/list/TaskListScreen.kt`
- `presentation/task/detail/TaskDetailScreen.kt`
- `presentation/task/addedit/AddEditTaskScreen.kt`
- `presentation/focus/FocusScreen.kt`
- `presentation/progress/ProgressScreen.kt`
- `presentation/profile/ProfileScreen.kt`
- `presentation/navigation/NavGraph.kt`

Layer ini bertugas menampilkan data ke pengguna dan menerima event dari pengguna. UI tidak langsung mengakses database atau API, tetapi membaca state dari ViewModel.

### ViewModel

ViewModel bertugas mengelola state UI, memanggil use case, dan menangani event dari screen.

Contoh ViewModel:

- `LoginViewModel`
- `RegisterViewModel`
- `DashboardViewModel`
- `TaskListViewModel`
- `TaskDetailViewModel`
- `AddEditTaskViewModel`
- `FocusViewModel`
- `ProgressViewModel`
- `ProfileViewModel`

State UI dikelola menggunakan `StateFlow`, lalu dikonsumsi oleh Jetpack Compose menggunakan lifecycle-aware state collection.

Contoh tanggung jawab ViewModel:

- `TaskListViewModel`: mengambil task, search task, filter priority, dan memilih active task.
- `AddEditTaskViewModel`: validasi form task, tambah task, dan edit task.
- `TaskDetailViewModel`: membaca detail task, toggle complete, dan delete task.
- `FocusViewModel`: menjalankan timer, pause, stop, break mode, complete task, dan focus lock.
- `ProgressViewModel`: menghitung weekly progress dan completed task.

### Domain Layer

Domain Layer berisi model utama aplikasi, repository interface, dan use case.

Lokasi:

```text
app/src/main/java/com/example/focusbuddyapp/domain
```

Struktur:

```text
domain
├── model
├── repository
└── usecase
```

Contoh domain model:

- `Task.kt`
- `User.kt`
- `FocusSession.kt`
- `SubTask.kt`
- `Quote.kt`

Contoh repository interface:

- `TaskRepository.kt`
- `AuthRepository.kt`
- `FocusSessionRepository.kt`
- `QuoteRepository.kt`

Contoh use case:

- `BrowseTasksUseCase`
- `ReadTaskUseCase`
- `AddTaskUseCase`
- `EditTaskUseCase`
- `DeleteTaskUseCase`
- `ToggleTaskCompleteUseCase`
- `LoginUseCase`
- `RegisterUseCase`
- `LogoutUseCase`
- `StartFocusSessionUseCase`
- `StopFocusSessionUseCase`
- `GetTodayFocusSummaryUseCase`
- `GetWeeklyFocusDataUseCase`

Use case membuat logic aplikasi lebih terstruktur karena setiap aksi memiliki tanggung jawab yang jelas.

### Data Layer

Data Layer berisi implementasi penyimpanan lokal, API service, DTO, mapper, repository implementation, dan preferences.

Lokasi:

```text
app/src/main/java/com/example/focusbuddyapp/data
```

Struktur:

```text
data
├── local
├── remote
├── repository
├── preferences
└── mapper
```

Data Layer bertanggung jawab mengambil data dari Room/API, menyimpan data, dan mengubah data dari entity/DTO ke domain model.

## 📂 Struktur Folder Project

Struktur folder utama:

```text
app
└── src
    └── main
        └── java
            └── com
                └── example
                    └── focusbuddyapp
                        ├── data
                        │   ├── local
                        │   │   ├── dao
                        │   │   └── entity
                        │   ├── remote
                        │   │   ├── api
                        │   │   ├── dto
                        │   │   └── interceptor
                        │   ├── repository
                        │   ├── preferences
                        │   └── mapper
                        │
                        ├── domain
                        │   ├── model
                        │   ├── repository
                        │   └── usecase
                        │
                        ├── presentation
                        │   ├── auth
                        │   ├── dashboard
                        │   ├── task
                        │   ├── focus
                        │   ├── progress
                        │   ├── profile
                        │   ├── splash
                        │   └── navigation
                        │
                        ├── ui
                        │   ├── components
                        │   ├── theme
                        │   └── util
                        │
                        ├── di
                        ├── MainActivity.kt
                        └── FocusBuddyApplication.kt
```

Penjelasan folder:

- `data/local`: konfigurasi Room Database, DAO, dan entity.
- `data/remote`: Retrofit API service, DTO, dan interceptor.
- `data/repository`: implementasi repository.
- `data/preferences`: DataStore Preferences.
- `data/mapper`: mapper untuk mengubah entity/DTO/domain model.
- `domain/model`: model utama aplikasi.
- `domain/repository`: kontrak repository.
- `domain/usecase`: logic aplikasi per fitur.
- `presentation`: screen, ViewModel, dan navigation.
- `ui/components`: reusable component seperti task card, bottom nav, timer component, dan priority chip.
- `ui/theme`: warna, typography, shape, dan theme aplikasi.
- `di`: dependency provider melalui `AppModule`.

## 🌐 API yang Digunakan

Project menyediakan konfigurasi Retrofit dan tiga API service: AuthApiService, TaskApiService, dan QuoteApiService. Pada implementasi saat ini, API service terutama berperan sebagai struktur remote layer. Fitur utama aplikasi tetap berjalan secara offline-first menggunakan Room Database dan DataStore.

Konfigurasi Retrofit berada di:

```text
app/src/main/java/com/example/focusbuddyapp/di/AppModule.kt
```

Base URL:

```text
https://api.focusbuddy.app/v1/
https://api.quotable.io/
```

### AuthApiService

Lokasi:

```text
data/remote/api/AuthApiService.kt
```

Endpoint:

```text
POST auth/login
POST auth/register
POST auth/logout
```

AuthApiService sudah tersedia sebagai struktur remote API, tetapi pada implementasi saat ini belum dipanggil oleh AuthRepositoryImpl. Login, register, logout, dan session pengguna berjalan secara lokal menggunakan Room Database dan DataStore Preferences.

### TaskApiService

Lokasi:

```text
data/remote/api/TaskApiService.kt
```

Endpoint:

```text
GET    tasks
GET    tasks/{id}
POST   tasks
PUT    tasks/{id}
DELETE tasks/{id}
PATCH  tasks/{id}/complete
```

TaskApiService tersedia untuk remote task endpoint. Pada implementasi saat ini, Browse, Read, Search, Filter, dan Toggle Complete berjalan dari Room Database. Add dan Edit menyimpan data ke Room terlebih dahulu, lalu mencoba sync ke API secara best-effort melalui createTask/updateTask. Delete menghapus data lokal dan hanya mencoba delete remote jika task memiliki remoteId. Method syncWithRemote tersedia, tetapi belum menjadi alur utama yang dipanggil dari UI.

### QuoteApiService

Lokasi:

```text
data/remote/api/QuoteApiService.kt
```

Endpoint:

```text
GET random?tags=inspirational,study
```

QuoteApiService tersedia untuk integrasi Quotable API, tetapi pada implementasi saat ini belum dipanggil oleh QuoteRepositoryImpl. Quote yang tampil di aplikasi diambil dari daftar quote lokal secara random.

## 🗄 Database Lokal

FocusBuddy menggunakan **Room Database** sebagai database lokal utama.

File database:

```text
data/local/AppDatabase.kt
```

Nama database:

```text
focusbuddy.db
```

Entity utama:

```text
UserEntity
TaskEntity
SubTaskEntity
FocusSessionEntity
```

DAO utama:

```text
UserDao
TaskDao
SubTaskDao
FocusSessionDao
```

### Tabel Database

#### users

Menyimpan data pengguna lokal.

Field utama:

- `id`
- `name`
- `email`
- `major`
- `avatar_url`
- `password`
- `created_at`

#### tasks

Menyimpan data tugas pengguna.

Field utama:

- `id`
- `title`
- `description`
- `category`
- `priority`
- `difficulty`
- `due_date`
- `due_time`
- `is_completed`
- `progress_percent`
- `study_notes`
- `remote_id`
- `synced_at`
- `user_id`
- `completed_at`
- `focus_duration`

#### subtasks

Menyimpan subtask dari task utama.

Field utama:

- `id`
- `parent_task_id`
- `title`
- `is_completed`
- `is_urgent`

#### focus_sessions

Menyimpan riwayat sesi fokus.

Field utama:

- `id`
- `linked_task_id`
- `duration_minutes`
- `start_time`
- `end_time`
- `break_duration_minutes`
- `remote_id`
- `synced_at`

## 💾 DataStore Preferences

FocusBuddy menggunakan **DataStore Preferences** untuk menyimpan data kecil yang berkaitan dengan session dan preferensi pengguna.

Lokasi:

```text
data/preferences/UserPreferences.kt
```

Data yang disimpan:

- Auth token
- User id
- User name
- User email
- Pomodoro minutes
- Break minutes
- Notification enabled
- Profile photo URI
- Active task id
- Active task title
- Dark mode
- Demo data seeded flag
- Focus lock state

## 🔁 BREAD Task

FocusBuddy memenuhi fitur BREAD pada Task Management:

- **Browse**: melihat daftar task di Task List dan Dashboard.
- **Read**: melihat detail task di Task Detail.
- **Edit**: mengubah task melalui Add/Edit Task.
- **Add**: menambahkan task baru melalui Add/Edit Task.
- **Delete**: menghapus task melalui Task Detail.

Implementasi BREAD berada pada:

```text
domain/usecase/task
data/repository/TaskRepositoryImpl.kt
data/local/dao/TaskDao.kt
```

## 📡 Offline-First Strategy

FocusBuddy menerapkan pendekatan offline-first:

1. Data utama disimpan dan dibaca dari Room Database.
2. UI mendapatkan data dari Room melalui repository dan `Flow`.
3. Saat task ditambah atau diedit, data disimpan ke Room terlebih dahulu.
4. Pada fitur task, repository mencoba melakukan sync ke API secara best-effort untuk add/edit, dan delete remote hanya jika task memiliki remoteId.
5. Jika API gagal, aplikasi tetap berjalan menggunakan data lokal.

Pendekatan ini membuat aplikasi tetap stabil untuk demo UAS dan tetap dapat digunakan walaupun koneksi internet tidak tersedia.

## 🧪 State Management

State management menggunakan:

- `StateFlow`
- `MutableStateFlow`
- Coroutines
- Lifecycle-aware collection di Compose
- DataStore untuk state yang perlu bertahan

Contoh state yang dikelola:

- Loading state
- Error message
- Search query
- Selected priority filter
- Timer state
- Active task
- Break mode
- Focus lock
- Dark mode
- Profile photo
- Weekly analytics data

## 🔔 Notification

FocusBuddy memiliki notification helper untuk menampilkan notifikasi ketika sesi fokus selesai.

Lokasi:

```text
ui/util/NotificationHelper.kt
```

Notifikasi digunakan agar pengguna mengetahui bahwa sesi fokus telah selesai dan dapat melanjutkan ke break atau task berikutnya.

## ✅ Kesesuaian Dengan Kebutuhan UAS

Project ini memenuhi poin utama dokumentasi dan implementasi UAS:

- Memiliki lebih dari 6 screen.
- Memiliki halaman list recycle-able menggunakan `LazyColumn`.
- Memiliki halaman detail data.
- Memiliki fitur Browse, Read, Edit, Add, Delete.
- Memiliki API service menggunakan Retrofit.
- Memiliki local database menggunakan Room.
- Memiliki DataStore Preferences.
- Menggunakan MVVM.
- Memiliki pemisahan Presentation, Domain, dan Data Layer.
- Memiliki Repository.
- Memiliki Use Case.
- Memiliki ViewModel.
- Memiliki model/entity.
- Memiliki progress analytics.
- Memiliki fitur tambahan seperti dark mode, profile photo, focus lock, dan demo data.

## 📌 Catatan Pengembangan

Beberapa catatan teknis project:

- Room menggunakan `fallbackToDestructiveMigration()` untuk kebutuhan development dan demo UAS.
- Untuk production release, migration Room sebaiknya dibuat secara eksplisit agar data pengguna tidak hilang saat update aplikasi.
- API service sudah tersedia, tetapi fitur utama tetap berjalan secara offline-first menggunakan database lokal.
- Auth demo berjalan menggunakan user lokal di Room dan session di DataStore.

## 🔮 Rencana Pengembangan

Pengembangan lanjutan yang dapat dilakukan:

- Mengaktifkan backend production secara penuh.
- Menghubungkan `QuoteRepositoryImpl` langsung ke Quotable API.
- Menambahkan migration Room resmi.
- Menambahkan unit test untuk use case, repository, dan ViewModel.
- Menambahkan sync dua arah antara Room dan server.
- Menambahkan UI subtask secara lebih lengkap.
- Menambahkan reminder berdasarkan deadline task.
- Menambahkan export laporan progress belajar.
- Menambahkan statistik lanjutan seperti streak, best focus hour, dan completion trend.

## 👥 Anggota Kelompok

- Muhammad Rizky Akbar - 2410817210016
- Orlando Sugian - 2410817210017

## 📄 Lisensi

Project ini dibuat untuk kebutuhan akademik UAS Pemrograman Mobile.
