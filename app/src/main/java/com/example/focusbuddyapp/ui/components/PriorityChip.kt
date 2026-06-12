package com.example.focusbuddyapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.focusbuddyapp.domain.model.Priority
import com.example.focusbuddyapp.ui.theme.*

@Composable
fun PriorityChip(priority: Priority) {
    val (bg, text) = when (priority) {
        Priority.HIGH   -> PriorityHighBg to PriorityHighText
        Priority.MEDIUM -> PriorityMedBg to PriorityMedText
        Priority.LOW    -> PriorityLowBg to PriorityLowText
    }
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bg,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(
            text = priority.name,
            color = text,
            style = FocusBuddyTypography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}
