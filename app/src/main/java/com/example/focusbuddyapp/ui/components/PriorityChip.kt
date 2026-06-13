package com.example.focusbuddyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.focusbuddyapp.domain.model.Priority
import com.example.focusbuddyapp.ui.theme.*

@Composable
fun PriorityChip(priority: Priority, modifier: Modifier = Modifier) {
    val (bg, text) = when (priority) {
        Priority.HIGH   -> PriorityHighBg to PriorityHighText
        Priority.MEDIUM -> PriorityMedBg  to PriorityMedText
        Priority.LOW    -> PriorityLowBg  to PriorityLowText
    }
    Text(
        text = priority.name,
        style = MaterialTheme.typography.labelMedium,
        color = text,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
