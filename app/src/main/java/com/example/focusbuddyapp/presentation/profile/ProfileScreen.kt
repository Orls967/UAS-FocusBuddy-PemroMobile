package com.example.focusbuddyapp.presentation.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.focusbuddyapp.ui.theme.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, onLogout: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                try {
                    val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    context.contentResolver.takePersistableUriPermission(uri, flag)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                viewModel.setProfilePhoto(uri.toString())
            }
        }
    )

    var showFaqDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    if (showFaqDialog) {
        AlertDialog(
            onDismissRequest = { showFaqDialog = false },
            title = { Text("FAQ & Support") },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("1. Bagaimana cara memulai Focus Session?", fontWeight = FontWeight.Bold)
                    Text("Pilih tugas dari daftar tugas Anda, lalu buka tab Focus dan tekan tombol 'Start Focus'.")
                    Spacer(Modifier.height(8.dp))
                    Text("2. Apa itu Mode Break?", fontWeight = FontWeight.Bold)
                    Text("Setelah sesi focus selesai, Anda akan ditawarkan untuk memulai waktu istirahat (Break) agar otak Anda tetap segar.")
                    Spacer(Modifier.height(8.dp))
                    Text("3. Mengapa data saya terhapus?", fontWeight = FontWeight.Bold)
                    Text("Aplikasi ini menyimpan data secara lokal. Jika Anda logout atau menghapus data aplikasi, data tugas Anda mungkin akan terhapus.")
                }
            },
            confirmButton = {
                TextButton(onClick = { showFaqDialog = false }) {
                    Text("Tutup")
                }
            }
        )
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Privacy Policy") },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("Kami menghormati privasi Anda. FocusBuddy berkomitmen melindungi informasi pribadi Anda:")
                    Spacer(Modifier.height(8.dp))
                    Text("• Penyimpanan Lokal: Semua data tugas dan sesi focus Anda disimpan di dalam database lokal perangkat Anda.")
                    Text("• Sinkronisasi Opsional: Jika fitur cloud diaktifkan, data Anda disinkronkan dengan server kami secara aman.")
                    Text("• Keamanan Akun: Password Anda disimpan menggunakan enkripsi satu arah yang aman.")
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("Tutup")
                }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("About FocusBuddy") },
            text = {
                Column {
                    Text("FocusBuddy App", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text("Versi 1.0.0-UAS")
                    Spacer(Modifier.height(8.dp))
                    Text("Aplikasi produktivitas cerdas yang membantu mahasiswa dan akademisi mengelola tugas dan meningkatkan fokus belajar menggunakan teknik Pomodoro.")
                    Spacer(Modifier.height(8.dp))
                    Text("Dikembangkan untuk memenuhi syarat UAS Pemrograman Mobile.")
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("Tutup")
                }
            }
        )
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // Header
            Text("Profile", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)

            Spacer(Modifier.height(20.dp))

            // Avatar + name card
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
                Row(Modifier.padding(20.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.profilePhotoUri != null) {
                            AsyncImage(
                                model = uiState.profilePhotoUri,
                                contentDescription = "Profile Photo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Filled.Person, null, tint = PrimaryNavy, modifier = Modifier.size(40.dp))
                        }

                        // Small camera edit icon overlay at the bottom right of the avatar circle
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(22.dp)
                                .background(PrimaryNavy, CircleShape)
                                .border(1.5.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CameraAlt,
                                contentDescription = "Change Photo",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(uiState.user?.name ?: "Scholar", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                        Text(uiState.user?.email ?: "", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(4.dp))
                        Surface(shape = RoundedCornerShape(4.dp), color = PriorityHighBg) {
                            Text("Academic Focus Champion", style = MaterialTheme.typography.labelSmall, color = PriorityHighText, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Stats
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
                Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    ProfileStat("Focus Time", "${uiState.totalFocusMinutes / 60}h ${uiState.totalFocusMinutes % 60}m")
                    Divider(modifier = Modifier.width(1.dp).height(40.dp), color = MaterialTheme.colorScheme.outline)
                    ProfileStat("Consistency", "87%")
                    Divider(modifier = Modifier.width(1.dp).height(40.dp), color = MaterialTheme.colorScheme.outline)
                    ProfileStat("Top Rank", "TOP 5")
                }
            }

            Spacer(Modifier.height(16.dp))

            // Pomodoro settings
            Text("Focus Settings", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Pomodoro Duration", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
                        Text("${uiState.pomodoroMinutes} min", style = MaterialTheme.typography.labelLarge, color = PrimaryNavy)
                    }
                    Slider(
                        value = uiState.pomodoroMinutes.toFloat(),
                        onValueChange = { viewModel.setPomodoroMinutes(it.toInt()) },
                        valueRange = 15f..60f,
                        steps = 8,
                        colors = SliderDefaults.colors(thumbColor = PrimaryNavy, activeTrackColor = PrimaryNavy, inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant)
                    )

                    Divider(Modifier.padding(vertical = 8.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Break Duration", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
                        Text("${uiState.breakMinutes} min", style = MaterialTheme.typography.labelLarge, color = PrimaryNavy)
                    }
                    Slider(
                        value = uiState.breakMinutes.toFloat(),
                        onValueChange = { viewModel.setBreakMinutes(it.toInt()) },
                        valueRange = 5f..20f,
                        steps = 2,
                        colors = SliderDefaults.colors(thumbColor = PrimaryNavy, activeTrackColor = PrimaryNavy, inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant)
                    )

                    Divider(Modifier.padding(vertical = 8.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Notifications", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
                        Switch(
                            checked = uiState.notificationsEnabled,
                            onCheckedChange = viewModel::setNotificationsEnabled,
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryNavy)
                        )
                    }

                    Divider(Modifier.padding(vertical = 8.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Dark Mode", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
                        Switch(
                            checked = uiState.isDarkMode,
                            onCheckedChange = viewModel::setDarkMode,
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryNavy)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Menu items
            ProfileMenuItem(icon = Icons.Filled.Help, label = "FAQ & Support", onClick = { showFaqDialog = true })
            Spacer(Modifier.height(8.dp))
            ProfileMenuItem(icon = Icons.Filled.PrivacyTip, label = "Privacy Policy", onClick = { showPrivacyDialog = true })
            Spacer(Modifier.height(8.dp))
            ProfileMenuItem(icon = Icons.Filled.Info, label = "About FocusBuddy", onClick = { showAboutDialog = true })

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { viewModel.logout(); onLogout() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTerracotta)
            ) {
                Icon(Icons.Filled.Logout, null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Log Out", color = Color.White)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ProfileStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ProfileMenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(12.dp))
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(1f))
            Icon(Icons.Filled.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
