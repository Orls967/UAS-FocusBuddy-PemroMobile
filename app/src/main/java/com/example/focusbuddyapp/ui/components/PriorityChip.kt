package com.example.focusbuddyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.focusbuddyapp.domain.model.Priority
import com.example.focusbuddyapp.ui.theme.*

@Composable
fun PriorityChip(priority: Priority, modifier: Modifier = Modifier) {
    val isDark = MaterialTheme.colorScheme.primary == DarkPrimary
    val bg: Color
    val text: Color
    
    when (priority) {
        Priority.HIGH -> {
            if (isDark) {
                bg = Color(0xFF4A1F1F)
                text = Color(0xFFFFB4AB)
            } else {
                bg = PriorityHighBg
                text = PriorityHighText
            }
        }
        Priority.MEDIUM -> {
            if (isDark) {
                bg = Color(0xFF383530)
                text = Color(0xFFD3C5B5)
            } else {
                bg = PriorityMedBg
                text = PriorityMedText
            }
        }
        Priority.LOW -> {
            if (isDark) {
                bg = Color(0xFF1C2D42)
                text = Color(0xFFBAC8DB)
            } else {
                bg = PriorityLowBg
                text = PriorityLowText
            }
        }
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
