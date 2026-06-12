package com.example.focusbuddyapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.focusbuddyapp.ui.theme.*

@Composable
fun CircularTimer(
    progress: Float,
    timeString: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Background arc
            drawCircle(
                color = SurfaceContainer,
                style = Stroke(width = 12.dp.toPx())
            )
            // Progress arc
            drawArc(
                color = PrimaryTerracotta,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text = timeString,
            style = FocusBuddyTypography.displayLarge,
            color = PrimaryNavy
        )
    }
}
