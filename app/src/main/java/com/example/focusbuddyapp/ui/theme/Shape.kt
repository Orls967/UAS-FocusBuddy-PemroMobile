package com.example.focusbuddyapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val FocusBuddyShapes = Shapes(
    // Chips, inputs, tags
    extraSmall = RoundedCornerShape(8.dp),
    // Buttons, list items
    small = RoundedCornerShape(12.dp),
    // Standard cards
    medium = RoundedCornerShape(16.dp),
    // Large cards, modals, bottom sheets
    large = RoundedCornerShape(24.dp),
    // Circular / pill elements
    extraLarge = RoundedCornerShape(50)
)
