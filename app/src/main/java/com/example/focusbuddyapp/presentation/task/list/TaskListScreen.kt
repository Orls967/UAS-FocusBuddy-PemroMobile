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
    onNavigateToAddTask: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filters = listOf("ALL", "HIGH", "MEDIUM", "LOW", "DONE")

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
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // Header
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(44.dp).clip(CircleShape).background(SurfaceDim),
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
                Text("Hello, ${uiState.userName.ifBlank { "Scholar" }}", style = MaterialTheme.typography.titleLarge, color = PrimaryText)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Filled.Settings, null, tint = SecondaryText)
            }

            Spacer(Modifier.height(16.dp))

            // Search bar
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search tasks...", color = SecondaryText) },
                leadingIcon = { Icon(Icons.Filled.Search, null, tint = SecondaryText) },
                singleLine = true,
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryNavy,
                    unfocusedBorderColor = OutlineVariant,
                    focusedContainerColor = SurfaceWhite,
                    unfocusedContainerColor = SurfaceWhite
                )
            )

            Spacer(Modifier.height(10.dp))

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
                            containerColor = SurfaceContainer,
                            labelColor = SecondaryText
                        )
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Queue header
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Task Queue", style = MaterialTheme.typography.titleMedium, color = PrimaryText)
                Text(
                    "${uiState.filteredTasks.size} Tasks remaining",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryText
                )
            }

            Spacer(Modifier.height(8.dp))

            // ─── LazyColumn (academic requirement) ──────────────────────────
            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryNavy)
                }
            } else if (uiState.filteredTasks.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.Assignment, null, tint = SecondaryText, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(12.dp))
                        Text("No tasks found", style = MaterialTheme.typography.bodyLarge, color = SecondaryText)
                        Text("Tap + to add your first task", style = MaterialTheme.typography.bodyMedium, color = SecondaryText)
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(
                        items = uiState.filteredTasks,
                        key = { task -> task.id }
                    ) { task ->
                        TaskCard(
                            task = task,
                            onCheckedChange = { isChecked -> viewModel.toggleComplete(task.id, isChecked) },
                            onClick = { onNavigateToTask(task.id) }
                        )
                    }
                }
            }
        }
    }
}
