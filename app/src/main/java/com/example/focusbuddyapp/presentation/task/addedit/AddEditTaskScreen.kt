package com.example.focusbuddyapp.presentation.task.addedit

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import com.example.focusbuddyapp.domain.model.Priority
import com.example.focusbuddyapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    viewModel: AddEditTaskViewModel,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaved()
    }

    val title = if (uiState.isEditMode) "Edit Task" else "New Task"
    val categories = listOf("Academic Focus", "Research", "Personal", "Group Project", "Exam Prep")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onCancel) { Icon(Icons.Filled.Close, "Cancel") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Main form card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(20.dp)) {

                    // Task Title
                    Text("Task Title", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = uiState.title,
                        onValueChange = viewModel::onTitleChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Advanced Microeconomics Research", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryNavy, unfocusedBorderColor = OutlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.4f), unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.4f)
                        )
                    )

                    Spacer(Modifier.height(16.dp))

                    // Category dropdown
                    Text("Category", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(6.dp))
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                        OutlinedTextField(
                            value = uiState.category,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryNavy, unfocusedBorderColor = OutlineVariant,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.4f), unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.4f)
                            )
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            categories.forEach { cat ->
                                DropdownMenuItem(text = { Text(cat) }, onClick = {
                                    viewModel.onCategoryChange(cat)
                                    expanded = false
                                })
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Priority
                    Text("Priority Level", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Priority.values().forEach { priority ->
                            val selected = uiState.priority == priority
                            FilterChip(
                                selected = selected,
                                onClick = { viewModel.onPriorityChange(priority) },
                                label = { Text(priority.name, style = MaterialTheme.typography.labelMedium) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = when (priority) {
                                        Priority.HIGH   -> PriorityHighBg
                                        Priority.MEDIUM -> PriorityMedBg
                                        Priority.LOW    -> PriorityLowBg
                                    },
                                    selectedLabelColor = when (priority) {
                                        Priority.HIGH   -> PriorityHighText
                                        Priority.MEDIUM -> PriorityMedText
                                        Priority.LOW    -> PriorityLowText
                                    },
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Study Notes
                    Text("Study Notes", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = uiState.studyNotes,
                        onValueChange = viewModel::onStudyNotesChange,
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        placeholder = { Text("List specific resources or sub-tasks here...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryNavy, unfocusedBorderColor = OutlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.4f), unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.4f)
                        )
                    )
                }
            }

            if (uiState.errorMessage != null) {
                Spacer(Modifier.height(8.dp))
                Text(uiState.errorMessage!!, color = ErrorRed, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(20.dp))

            // Save button
            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Filled.Save, null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Save Task", style = MaterialTheme.typography.titleMedium, color = Color.White)
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
