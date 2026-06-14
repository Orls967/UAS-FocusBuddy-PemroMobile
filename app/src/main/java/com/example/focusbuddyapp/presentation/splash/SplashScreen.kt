package com.example.focusbuddyapp.presentation.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import com.example.focusbuddyapp.ui.theme.DarkNavy
import com.example.focusbuddyapp.ui.theme.PrimaryNavy
import com.example.focusbuddyapp.ui.theme.WarmBackground
import com.example.focusbuddyapp.ui.theme.WarmBeige

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Pulse animation for logo
    val infiniteTransition = rememberInfiniteTransition(label = "splash_pulse")
    val logoScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_scale"
    )

    // Navigation effect
    LaunchedEffect(uiState) {
        when (uiState) {
            SplashUiState.NavigateToLogin     -> onNavigateToLogin()
            SplashUiState.NavigateToDashboard -> onNavigateToDashboard()
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(PrimaryNavy, DarkNavy)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo circle
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(logoScale)
                    .clip(CircleShape)
                    .background(WarmBeige.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    androidx.compose.material.icons.Icons.Filled.School,
                    contentDescription = null,
                    tint = WarmBeige,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "FocusBuddy",
                style = MaterialTheme.typography.displayLarge,
                color = Color.White
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Stay Focused, Stay Productive",
                style = MaterialTheme.typography.bodyLarge,
                color = WarmBeige.copy(alpha = 0.7f)
            )

            Spacer(Modifier.height(64.dp))

            // Progress indicator
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LinearProgressIndicator(
                    modifier = Modifier.width(160.dp),
                    color = WarmBeige,
                    trackColor = WarmBeige.copy(alpha = 0.25f)
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "INITIALIZING",
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmBeige.copy(alpha = 0.5f),
                    letterSpacing = 3.sp
                )
            }
        }
    }
}
