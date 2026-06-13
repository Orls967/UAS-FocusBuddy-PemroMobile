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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.focusbuddyapp.domain.model.Task
import com.example.focusbuddyapp.ui.components.PriorityChip
import com.example.focusbuddyapp.ui.theme.*

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToAddTask: () -> Unit,
    onNavigateToTask: (Int) -> Unit,
    onNavigateToTimer: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusProgress = (uiState.todayFocusMinutes.toFloat() / uiState.dailyGoalMinutes).coerceIn(0f, 1f)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddTask,
                containerColor = PrimaryNavy,
                contentColor = Color.White,
                shape = CircleShape
            ) { Icon(Icons.Filled.Add, "Add Task") }
        },
        containerColor = WarmBackground
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
                    Modifier.size(44.dp).clip(CircleShape).background(SurfaceDim),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.Person, null, tint = PrimaryNavy) }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Hello, Scholar", style = MaterialTheme.typography.titleLarge, color = PrimaryText)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.Settings, null, tint = SecondaryText)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Tasks completed card
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(SurfaceWhite), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("TASKS", style = MaterialTheme.typography.labelMedium, color = SecondaryText)
                        Icon(Icons.Filled.Assignment, null, tint = SecondaryText, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.height(4.dp))
                    Text("${uiState.completedTodayCount}", style = MaterialTheme.typography.headlineLarge, color = PrimaryText)
                    Text("Completed Today", style = MaterialTheme.typography.bodyMedium, color = PrimaryText)
                    Text("${uiState.totalThisWeekCount} Total This Week", style = MaterialTheme.typography.bodySmall, color = SecondaryText)
                }
            }

            Spacer(Modifier.height(12.dp))

            // Focus progress card
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(SurfaceWhite), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("YOUR PROGRESS", style = MaterialTheme.typography.labelMedium, color = SecondaryText)
                        Icon(Icons.Filled.Timer, null, tint = SecondaryText, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.height(4.dp))
                    val focusHours = uiState.todayFocusMinutes / 60
                    val focusMin = uiState.todayFocusMinutes % 60
                    val goalHours = uiState.dailyGoalMinutes / 60
                    val goalMin = uiState.dailyGoalMinutes % 60
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("${focusHours}h", style = MaterialTheme.typography.headlineLarge, color = PrimaryText)
                        Spacer(Modifier.width(4.dp))
                        Text("${goalHours}h ${goalMin}m", style = MaterialTheme.typography.titleMedium, color = PrimaryTerracotta)
                    }
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { focusProgress },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(50)),
                        color = PrimaryNavy,
                        trackColor = SurfaceContainer
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("${uiState.weeklyFocusMinutes / 60}h Total This Week", style = MaterialTheme.typography.bodySmall, color = SecondaryText)
                }
            }

            Spacer(Modifier.height(12.dp))

            // Start Focus CTA
            Card(
                modifier = Modifier.fillMaxWidth().clickable(onClick = onNavigateToTimer),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryNavy),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.PlayArrow, null, tint = Color.White, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Start Focusing", style = MaterialTheme.typography.titleLarge, color = Color.White)
                        Text("Deep work session: 45 min", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.7f))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Motivational quote
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmBeige),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("99", style = MaterialTheme.typography.labelLarge.copy(fontSize = 20.sp), color = PrimaryTerracotta)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "\"${uiState.quote.content}\"",
                        style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                        color = PrimaryText
                    )
                    Spacer(Modifier.height(6.dp))
                    Text("— ${uiState.quote.author}", style = MaterialTheme.typography.labelMedium, color = SecondaryText)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Today's tasks
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Today's Tasks", style = MaterialTheme.typography.titleLarge, color = PrimaryText)
                TextButton(onClick = {}) { Text("View All →", color = PrimaryTerracotta) }
            }

            Spacer(Modifier.height(8.dp))

            if (uiState.todayTasks.isEmpty()) {
                Text("No tasks for today. Add one!", color = SecondaryText, style = MaterialTheme.typography.bodyMedium)
            } else {
                uiState.todayTasks.forEach { task ->
                    DashboardTaskItem(task = task, onClick = { onNavigateToTask(task.id) })
                    Spacer(Modifier.height(8.dp))
                }
            }

            // Logout
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { viewModel.logout(); onLogout() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTerracotta)
            ) { Text("Log Out", color = Color.White) }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DashboardTaskItem(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(SurfaceWhite),
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
                Text(task.title, style = MaterialTheme.typography.titleSmall, color = PrimaryText, maxLines = 2)
                if (task.description.isNotBlank())
                    Text(task.description, style = MaterialTheme.typography.bodySmall, color = SecondaryText, maxLines = 1)
            }
            Spacer(Modifier.width(8.dp))
            PriorityChip(priority = task.priority)
        }
    }
}
