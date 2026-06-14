package com.example.focusbuddyapp.presentation.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.items
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

    if (uiState.showNextTaskModal) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissNextTaskModal() },
            title = { Text("Pilih Tugas Berikutnya", style = MaterialTheme.typography.titleLarge) },
            text = {
                if (uiState.activeTasks.isEmpty()) {
                    Text("Tidak ada tugas aktif di antrean.", style = MaterialTheme.typography.bodyMedium)
                } else {
                    androidx.compose.foundation.lazy.LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)
                    ) {
                        items(uiState.activeTasks) { task ->
                            var showDetail by remember { mutableStateOf(false) }
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(task.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                            Spacer(Modifier.height(4.dp))
                                            com.example.focusbuddyapp.ui.components.PriorityChip(priority = task.priority)
                                        }
                                        Spacer(Modifier.width(8.dp))
                                        Button(
                                            onClick = { viewModel.selectNextTaskAndStopBreak(task) },
                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text("Pilih", color = Color.White)
                                        }
                                    }
                                    if (task.description.isNotBlank()) {
                                        Spacer(Modifier.height(4.dp))
                                        TextButton(
                                            onClick = { showDetail = !showDetail },
                                            contentPadding = PaddingValues(0.dp)
                                        ) {
                                            Text(if (showDetail) "Sembunyikan Detail" else "Lihat Detail", style = MaterialTheme.typography.labelSmall)
                                        }
                                        if (showDetail) {
                                            Text(task.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissNextTaskModal() }) {
                    Text("Batal")
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

            if (uiState.isBreakMode) {
                Spacer(Modifier.height(10.dp))
                Button(
                    onClick = { viewModel.showNextTaskModal() },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Filled.PlayArrow, null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Start Next Task", color = Color.White)
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
