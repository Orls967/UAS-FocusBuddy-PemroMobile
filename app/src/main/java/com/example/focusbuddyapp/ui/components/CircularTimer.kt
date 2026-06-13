package com.example.focusbuddyapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.focusbuddyapp.ui.theme.PrimaryNavy
import com.example.focusbuddyapp.ui.theme.WarmBeige

@Composable
fun CircularTimer(
    timeText: String,
    label: String,
    progress: Float,           // 0f..1f
    size: Dp = 240.dp,
    strokeWidth: Dp = 12.dp,
    trackColor: Color = WarmBeige,
    progressColor: Color = PrimaryNavy,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val strokePx = strokeWidth.toPx()
            val inset = strokePx / 2f
            val arcRect = Size(this.size.width - strokePx, this.size.height - strokePx)

            // Track (background arc)
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcRect,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // Progress arc
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * progress.coerceIn(0f, 1f),
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcRect,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }

        // Center content
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = com.example.focusbuddyapp.ui.theme.SecondaryText
            )
            Text(
                text = timeText,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 42.sp),
                color = PrimaryNavy
            )
        }
    }
}
