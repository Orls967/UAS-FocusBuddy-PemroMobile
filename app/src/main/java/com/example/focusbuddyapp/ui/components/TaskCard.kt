package com.example.focusbuddyapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.focusbuddyapp.domain.model.Task
import com.example.focusbuddyapp.ui.theme.*

@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                PriorityChip(priority = task.priority)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = PrimaryText
                )
                if (task.category.isNotBlank()) {
                    Text(
                        text = task.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = SecondaryText
                    )
                }
            }
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = null,
                colors = CheckboxDefaults.colors(checkedColor = SuccessGreen)
            )
        }
    }
}
