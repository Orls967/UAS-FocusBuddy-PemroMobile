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

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, onLogout: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(containerColor = WarmBackground) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // Header
            Text("Profile", style = MaterialTheme.typography.headlineMedium, color = PrimaryText)

            Spacer(Modifier.height(20.dp))

            // Avatar + name card
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(SurfaceWhite), elevation = CardDefaults.cardElevation(2.dp)) {
                Row(Modifier.padding(20.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(72.dp).clip(CircleShape).background(SurfaceDim), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.Person, null, tint = PrimaryNavy, modifier = Modifier.size(40.dp))
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(uiState.user?.name ?: "Scholar", style = MaterialTheme.typography.titleLarge, color = PrimaryText, fontWeight = FontWeight.Bold)
                        Text(uiState.user?.email ?: "", style = MaterialTheme.typography.bodyMedium, color = SecondaryText)
                        Spacer(Modifier.height(4.dp))
                        Surface(shape = RoundedCornerShape(4.dp), color = PriorityHighBg) {
                            Text("Academic Focus Champion", style = MaterialTheme.typography.labelSmall, color = PriorityHighText, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Stats
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(SurfaceWhite), elevation = CardDefaults.cardElevation(2.dp)) {
                Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    ProfileStat("Focus Time", "${uiState.totalFocusMinutes / 60}h ${uiState.totalFocusMinutes % 60}m")
                    Divider(modifier = Modifier.width(1.dp).height(40.dp), color = OutlineVariant)
                    ProfileStat("Consistency", "87%")
                    Divider(modifier = Modifier.width(1.dp).height(40.dp), color = OutlineVariant)
                    ProfileStat("Top Rank", "TOP 5")
                }
            }

            Spacer(Modifier.height(16.dp))

            // Pomodoro settings
            Text("Focus Settings", style = MaterialTheme.typography.titleMedium, color = PrimaryText, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(SurfaceWhite), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Pomodoro Duration", style = MaterialTheme.typography.bodyMedium, color = PrimaryText)
                        Text("${uiState.pomodoroMinutes} min", style = MaterialTheme.typography.labelLarge, color = PrimaryNavy)
                    }
                    Slider(
                        value = uiState.pomodoroMinutes.toFloat(),
                        onValueChange = { viewModel.setPomodoroMinutes(it.toInt()) },
                        valueRange = 15f..60f,
                        steps = 8,
                        colors = SliderDefaults.colors(thumbColor = PrimaryNavy, activeTrackColor = PrimaryNavy, inactiveTrackColor = SurfaceContainer)
                    )

                    Divider(Modifier.padding(vertical = 8.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Break Duration", style = MaterialTheme.typography.bodyMedium, color = PrimaryText)
                        Text("${uiState.breakMinutes} min", style = MaterialTheme.typography.labelLarge, color = PrimaryNavy)
                    }
                    Slider(
                        value = uiState.breakMinutes.toFloat(),
                        onValueChange = { viewModel.setBreakMinutes(it.toInt()) },
                        valueRange = 5f..20f,
                        steps = 2,
                        colors = SliderDefaults.colors(thumbColor = PrimaryNavy, activeTrackColor = PrimaryNavy, inactiveTrackColor = SurfaceContainer)
                    )

                    Divider(Modifier.padding(vertical = 8.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Notifications", style = MaterialTheme.typography.bodyMedium, color = PrimaryText)
                        Switch(
                            checked = uiState.notificationsEnabled,
                            onCheckedChange = viewModel::setNotificationsEnabled,
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryNavy)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Menu items
            ProfileMenuItem(icon = Icons.Filled.Help, label = "FAQ & Support")
            Spacer(Modifier.height(8.dp))
            ProfileMenuItem(icon = Icons.Filled.PrivacyTip, label = "Privacy Policy")

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
        Text(value, style = MaterialTheme.typography.titleMedium, color = PrimaryText, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = SecondaryText)
    }
}

@Composable
private fun ProfileMenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable {},
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(SurfaceWhite),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = SecondaryText, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(12.dp))
            Text(label, style = MaterialTheme.typography.bodyMedium, color = PrimaryText, modifier = Modifier.weight(1f))
            Icon(Icons.Filled.ChevronRight, null, tint = SecondaryText)
        }
    }
}
