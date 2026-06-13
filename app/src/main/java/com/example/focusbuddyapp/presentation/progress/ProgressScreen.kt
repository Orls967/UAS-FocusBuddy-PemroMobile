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
fun ProgressScreen(viewModel: ProgressViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val maxMinutes = uiState.weeklyData.values.maxOrNull()?.takeIf { it > 0 } ?: 1

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
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("My Progress", style = MaterialTheme.typography.headlineMedium, color = PrimaryText, modifier = Modifier.weight(1f))
                Icon(Icons.Filled.Settings, null, tint = SecondaryText)
            }

            Spacer(Modifier.height(20.dp))

            // Weekly Focus bar chart card
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(SurfaceWhite), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Weekly Focus", style = MaterialTheme.typography.titleLarge, color = PrimaryText)
                    Text("Your productivity trend", style = MaterialTheme.typography.bodySmall, color = SecondaryText)
                    Spacer(Modifier.height(16.dp))

                    // Bar chart
                    Row(
                        Modifier.fillMaxWidth().height(120.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        days.forEach { day ->
                            val minutes = uiState.weeklyData[day.uppercase()] ?: 0
                            val ratio = minutes.toFloat() / maxMinutes
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .width(28.dp)
                                        .height((100 * ratio + 4).dp)
                                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                        .background(if (ratio > 0) PrimaryNavy else SurfaceContainer)
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(day.take(3), style = MaterialTheme.typography.labelSmall, color = if (minutes > 0 && minutes == uiState.weeklyData.values.max()) PrimaryText else SecondaryText, fontWeight = if (minutes == uiState.weeklyData.values.maxOrNull()) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Total Focus + Best Day row
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(SurfaceWhite), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Total Focus", style = MaterialTheme.typography.titleMedium, color = PrimaryText, fontWeight = FontWeight.Bold)
                        Text(": ${uiState.totalFocusMinutes / 60}h", style = MaterialTheme.typography.headlineSmall, color = PrimaryText)
                        Text("Tasks Completed: ${uiState.completedTaskCount}", style = MaterialTheme.typography.bodySmall, color = SecondaryText)
                        Spacer(Modifier.height(4.dp))
                        LinearProgressIndicator(progress = { (uiState.consistencyPercent / 100f) }, modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(50)), color = PrimaryNavy, trackColor = SurfaceContainer)
                    }
                }
                Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(SurfaceWhite), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Icon(Icons.Filled.Star, null, tint = PrimaryTerracotta, modifier = Modifier.size(20.dp))
                        Text("Weekly Best", style = MaterialTheme.typography.titleMedium, color = PrimaryText)
                        Text(": ", style = MaterialTheme.typography.bodySmall, color = SecondaryText)
                        Text(uiState.bestDay, style = MaterialTheme.typography.headlineSmall, color = PrimaryText, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Insight card
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(SurfaceWhite), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Lightbulb, null, tint = PrimaryNavy, modifier = Modifier.size(32.dp))
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "You've Spent ${(uiState.totalFocusMinutes / 60f).toInt()} Hours, Working on a Task at ${uiState.bestDay}!",
                        style = MaterialTheme.typography.titleMedium,
                        color = PrimaryText,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(uiState.insightText, style = MaterialTheme.typography.bodyMedium, color = SecondaryText)
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
                    ) { Text("Keep Going!!", color = Color.White) }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Stats row
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(SurfaceWhite), elevation = CardDefaults.cardElevation(2.dp)) {
                Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    StatItem("CONSISTENCY", "${uiState.consistencyPercent}%")
                    StatItem("DEEP WORK", "${String.format("%.1f", uiState.deepWorkHours)}h")
                    Box {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("RANK", style = MaterialTheme.typography.labelMedium, color = SecondaryText)
                            Surface(shape = RoundedCornerShape(4.dp), color = PrimaryTerracotta) {
                                Text(uiState.rank, style = MaterialTheme.typography.labelLarge, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = SecondaryText)
        Text(value, style = MaterialTheme.typography.titleLarge, color = PrimaryText, fontWeight = FontWeight.Bold)
    }
}
