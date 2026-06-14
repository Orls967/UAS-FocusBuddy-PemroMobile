package com.example.focusbuddyapp.presentation.task.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.focusbuddyapp.ui.components.TaskCard
import com.example.focusbuddyapp.ui.theme.*
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip

@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel,
    onNavigateToTask: (Int) -> Unit,
    onNavigateToAddTask: () -> Unit,
    onNavigateToTimer: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filters = listOf("ALL", "HIGH", "MEDIUM", "LOW")

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddTask,
                containerColor = PrimaryNavy,
                contentColor = Color.White,
                shape = CircleShape
            ) { Icon(Icons.Filled.Add, "Add Task") }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
        ) {
            item {
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
                            Icon(Icons.Filled.Person, null, tint = PrimaryNavy)
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("Hello, ${uiState.userName.ifBlank { "Scholar" }}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                }
            }

            item {
                Spacer(Modifier.height(6.dp))
                // Search bar
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = viewModel::onSearchChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search tasks...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    leadingIcon = { Icon(Icons.Filled.Search, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryNavy,
                        unfocusedBorderColor = OutlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }

            item {
                // Filter chips row
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    filters.forEach { filter ->
                        val isSelected = uiState.selectedFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.onFilterChange(filter) },
                            label = { Text(filter, style = MaterialTheme.typography.labelMedium) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryNavy,
                                selectedLabelColor = Color.White,
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            item {
                // Queue header
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Task Queue", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                    Text(
                        "${uiState.activeTasks.size} Tasks remaining",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // ─── Dynamic Content ──────────────────────────
            if (uiState.isLoading) {
                item {
                    Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PrimaryNavy)
                    }
                }
            } else if (uiState.activeTasks.isEmpty() && uiState.completedTasks.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.Assignment, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(48.dp))
                            Spacer(Modifier.height(12.dp))
                            Text("No tasks found", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Tap + to add your first task", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            } else {
                if (uiState.activeTasks.isNotEmpty()) {
                    item {
                        Text("Active Tasks", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    }
                    items(
                        items = uiState.activeTasks,
                        key = { task -> "active_${task.id}" }
                    ) { task ->
                        TaskCard(
                            task = task,
                            onCheckedChange = {},
                            onClick = { onNavigateToTask(task.id) },
                            onStartFocus = { viewModel.startFocus(task, onNavigateToTimer) }
                        )
                    }
                }

                if (uiState.completedTasks.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(Modifier.height(12.dp))
                        Text("Completed Today", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    }
                    items(
                        items = uiState.completedTasks,
                        key = { task -> "completed_${task.id}" }
                    ) { task ->
                        TaskCard(
                            task = task,
                            onCheckedChange = {},
                            onClick = { onNavigateToTask(task.id) }
                        )
                    }
                }
            }
        }
    }
}
