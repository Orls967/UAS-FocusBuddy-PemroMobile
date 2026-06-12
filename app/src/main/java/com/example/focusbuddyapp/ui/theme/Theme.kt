package com.example.focusbuddyapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryNavy,
    onPrimary = SurfaceWhite,
    secondary = PrimaryTerracotta,
    onSecondary = SurfaceWhite,
    background = WarmBackground,
    onBackground = PrimaryText,
    surface = SurfaceWhite,
    onSurface = PrimaryText,
    error = ErrorRed
)

@Composable
fun FocusBuddyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Force light scheme to ensure consistent student presentation design layout
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = FocusBuddyTypography,
        shapes = FocusBuddyShapes,
        content = content
    )
}
