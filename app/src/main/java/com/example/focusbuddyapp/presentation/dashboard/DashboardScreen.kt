package com.example.focusbuddyapp.presentation.dashboard

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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.focusbuddyapp.domain.model.Task
import com.example.focusbuddyapp.ui.components.PriorityChip
import com.example.focusbuddyapp.ui.theme.*
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToAddTask: () -> Unit,
    onNavigateToTask: (Int) -> Unit,
    onNavigateToTimer: () -> Unit,
    onNavigateToTaskList: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusProgress = (uiState.todayFocusMinutes.toFloat() / uiState.dailyGoalMinutes).coerceIn(0f, 1f)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // Header
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(44.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant),
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
                        Icon(Icons.Filled.Person, null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Hello, ${uiState.userName.ifBlank { "Scholar" }}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Tasks completed card
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("TASKS", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(Modifier.height(4.dp))
                    Text("${uiState.completedTodayCount}", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onBackground)
                    Text("Completed Today", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
                    Text("${uiState.totalThisWeekCount} Total This Week", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(Modifier.height(12.dp))

            // Focus progress card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToProgress),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("YOUR PROGRESS", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(Modifier.height(4.dp))
                    val focusHours = uiState.todayFocusMinutes / 60
                    val focusMin = uiState.todayFocusMinutes % 60
                    val goalHours = uiState.dailyGoalMinutes / 60
                    val goalMin = uiState.dailyGoalMinutes % 60

                    val focusText = if (focusHours > 0 && focusMin > 0) "${focusHours}h ${focusMin}m"
                        else if (focusHours > 0) "${focusHours}h"
                        else "${focusMin}m"
                        
                    val goalText = if (goalHours > 0 && goalMin > 0) "${goalHours}h ${goalMin}m"
                        else if (goalHours > 0) "${goalHours}h"
                        else "${goalMin}m"

                    Text(focusText, style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onBackground)
                    Text("Today's Focus", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(4.dp))
                    Text("Goal: $goalText", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { focusProgress },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(50)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("${uiState.weeklyFocusMinutes / 60}h Total This Week", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(Modifier.height(12.dp))

            // Start Focus CTA
            Card(
                modifier = Modifier.fillMaxWidth().clickable(onClick = onNavigateToTimer),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.PlayArrow, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Start Focusing", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimary)
                        Text("Deep work session: 45 min", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Motivational quote
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "“",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 28.sp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "\"${uiState.quote.content}\"",
                            style = MaterialTheme.typography.titleMedium.copy(fontStyle = FontStyle.Italic, fontSize = 18.sp, lineHeight = 26.sp),
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            maxLines = 3,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                    Text(
                        text = "— ${uiState.quote.author}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Today's tasks
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Today's Tasks", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                TextButton(onClick = onNavigateToTaskList) { Text("View All →", color = MaterialTheme.colorScheme.secondary) }
            }

            Spacer(Modifier.height(8.dp))

            if (uiState.todayTasks.isEmpty()) {
                Text("No tasks for today. Add one!", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            } else {
                uiState.todayTasks.forEach { task ->
                    DashboardTaskItem(task = task, onClick = { onNavigateToTask(task.id) })
                    Spacer(Modifier.height(8.dp))
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DashboardTaskItem(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = null,
                colors = CheckboxDefaults.colors(uncheckedColor = OutlineVariant)
            )
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text(task.title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground, maxLines = 2)
                if (task.description.isNotBlank())
                    Text(task.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }
            Spacer(Modifier.width(8.dp))
            PriorityChip(priority = task.priority)
        }
    }
}
