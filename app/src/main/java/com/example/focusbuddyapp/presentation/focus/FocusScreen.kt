package com.example.focusbuddyapp.presentation.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.focusbuddyapp.ui.components.CircularTimer
import com.example.focusbuddyapp.ui.theme.*
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip

@Composable
fun FocusScreen(viewModel: FocusViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.showBreakDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissBreakDialog() },
            title = { Text("Focus Session Complete!") },
            text = { Text("Hebat! Sesi focus Anda telah selesai. Apakah Anda ingin memulai waktu istirahat selama ${uiState.breakDurationMinutes} menit?") },
            confirmButton = {
                Button(
                    onClick = { viewModel.startBreak() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Start Break")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.skipBreak() }) {
                    Text("Skip Break")
                }
            }
        )
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // App bar
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Focus Session", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
            }

            Spacer(Modifier.height(32.dp))

            // Circular timer
            CircularTimer(
                timeText = uiState.timerText,
                label = uiState.modeLabel,
                progress = uiState.progress,
                size = 260.dp,
                strokeWidth = 12.dp,
                progressColor = if (uiState.isBreakMode) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(24.dp))

            if (uiState.isBreakMode) {
                LinearProgressIndicator(
                    progress = { uiState.progress },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(50)),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(Modifier.height(16.dp))
            }

            // Current task card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("CURRENT TASK", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(4.dp))
                    Text(uiState.currentTaskTitle, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                }
            }

            Spacer(Modifier.height(24.dp))

            // Control buttons
            Button(
                onClick = viewModel::startTimer,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = uiState.timerState != TimerState.RUNNING,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Filled.PlayArrow, null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Start Focus", style = MaterialTheme.typography.titleMedium, color = Color.White)
            }

            Spacer(Modifier.height(10.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = viewModel::pauseTimer,
                    modifier = Modifier.weight(1f).height(48.dp),
                    enabled = uiState.timerState == TimerState.RUNNING,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.Pause, null)
                    Spacer(Modifier.width(4.dp))
                    Text("Pause")
                }
                Button(
                    onClick = { viewModel.stopTimer() },
                    modifier = Modifier.weight(1f).height(48.dp),
                    enabled = uiState.timerState != TimerState.IDLE,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(Icons.Filled.Stop, null, tint = Color.White)
                    Spacer(Modifier.width(4.dp))
                    Text("Stop", color = Color.White)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Break duration selector
            Text("Break Duration", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                listOf(5, 10).forEach { minutes ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = uiState.breakDurationMinutes == minutes,
                            onClick = { viewModel.setBreakDuration(minutes) },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                        )
                        Text("$minutes Minutes", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Today stats
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatBox(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.LocalFireDepartment,
                    value = "${uiState.todayFocusMinutes / 60}h ${uiState.todayFocusMinutes % 60}m",
                    label = "Today's Focus"
                )
                StatBox(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.MilitaryTech,
                    value = "${uiState.efficiencyPercent}%",
                    label = "Efficiency"
                )
            }
        }
    }
}

@Composable
private fun StatBox(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Card(modifier = modifier, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
