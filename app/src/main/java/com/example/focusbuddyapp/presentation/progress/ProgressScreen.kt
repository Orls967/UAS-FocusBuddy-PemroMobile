package com.example.focusbuddyapp.presentation.progress

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.focusbuddyapp.ui.theme.*

@Composable
fun ProgressScreen(viewModel: ProgressViewModel, onNavigateToTaskList: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val maxMinutes = uiState.weeklyData.values.maxOrNull()?.takeIf { it > 0 } ?: 1
    
    var selectedDay by remember { mutableStateOf<String?>(null) }

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
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("My Progress", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(20.dp))

            // Weekly Focus bar chart card
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Weekly Focus", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                    Text("Your productivity trend", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(16.dp))

                    // Bar chart
                    Row(
                        Modifier.fillMaxWidth().height(120.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        days.forEach { day ->
                            val dayKey = day.uppercase()
                            val minutes = uiState.weeklyData[dayKey] ?: 0
                            val ratio = minutes.toFloat() / maxMinutes
                            val isSelected = selectedDay == dayKey
                            
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable { selectedDay = dayKey }
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(28.dp)
                                        .height((100 * ratio + 4).dp)
                                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                        .background(
                                            if (ratio > 0) {
                                                MaterialTheme.colorScheme.primary.copy(
                                                    alpha = if (selectedDay != null && !isSelected) 0.4f else 1f
                                                )
                                            } else {
                                                MaterialTheme.colorScheme.surfaceVariant
                                            }
                                        )
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(day.take(3), style = MaterialTheme.typography.labelSmall, color = if (minutes > 0 && minutes == uiState.weeklyData.values.max()) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = if (minutes == uiState.weeklyData.values.maxOrNull()) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Total Focus + Best Day row
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Total Focus", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                        Text(": ${uiState.totalFocusMinutes / 60}h", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground)
                        Text("Tasks Completed: ${uiState.completedTaskCount}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(4.dp))
                        LinearProgressIndicator(progress = { (uiState.consistencyPercent / 100f) }, modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(50)), color = MaterialTheme.colorScheme.primary, trackColor = MaterialTheme.colorScheme.surfaceVariant)
                    }
                }
                Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Icon(Icons.Filled.Star, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
                        Text("Weekly Best", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                        Text(": ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(uiState.bestDay, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Insight card
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Lightbulb, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "You've Spent ${(uiState.totalFocusMinutes / 60f).toInt()} Hours, Working on a Task at ${uiState.bestDay}!",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(uiState.insightText, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = onNavigateToTaskList,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("Keep Going!!", color = MaterialTheme.colorScheme.onPrimary) }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Stats row
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
                Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    StatItem("COMPLETED", "${uiState.completedTaskCount}")
                    StatItem("SESSIONS", "${uiState.focusSessionsCount}")
                    StatItem("MINUTES", "${uiState.totalFocusMinutes}m")
                }
            }

            Spacer(Modifier.height(16.dp))
        }
        
        if (selectedDay != null) {
            val dayNameMap = mapOf(
                "MON" to "Monday", "TUE" to "Tuesday", "WED" to "Wednesday",
                "THU" to "Thursday", "FRI" to "Friday", "SAT" to "Saturday", "SUN" to "Sunday"
            )
            val fullDayName = dayNameMap[selectedDay] ?: selectedDay!!
            val minutes = uiState.weeklyData[selectedDay] ?: 0
            val tasks = uiState.tasksByDay[selectedDay] ?: emptyList()
            val sessions = tasks.size

            androidx.compose.ui.window.Dialog(onDismissRequest = { selectedDay = null }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 24.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = fullDayName, 
                            style = MaterialTheme.typography.headlineSmall, 
                            fontWeight = FontWeight.Bold, 
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.height(16.dp))
                        
                        Text("Total Focus : ${minutes}m", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
                        Spacer(Modifier.height(4.dp))
                        Text("Completed Tasks : ${tasks.size}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(4.dp))
                        Text("Sessions : $sessions", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        
                        Spacer(Modifier.height(16.dp))
                        
                        if (tasks.isNotEmpty()) {
                            Text("Tasks:", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
                            Spacer(Modifier.height(8.dp))
                            
                            Box(modifier = Modifier.weight(1f, fill = false)) {
                                androidx.compose.foundation.lazy.LazyColumn(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(tasks.size) { index ->
                                        val task = tasks[index]
                                        Text(
                                            text = "• ${task.title} (${task.focusDuration}m)",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            modifier = Modifier.padding(bottom = 6.dp)
                                        )
                                    }
                                }
                            }
                        } else {
                            Text("No tasks completed on this day.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        
                        Spacer(Modifier.height(24.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { selectedDay = null }) {
                                Text("Close", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
    }
}
