package com.example.focusbuddyapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val FocusBuddyColorScheme = lightColorScheme(
    primary             = PrimaryNavy,
    onPrimary           = SurfaceWhite,
    primaryContainer    = PrimaryNavyContainer,
    onPrimaryContainer  = SurfaceWhite,
    secondary           = PrimaryTerracotta,
    onSecondary         = SurfaceWhite,
    secondaryContainer  = PriorityHighBg,
    onSecondaryContainer = PriorityHighText,
    tertiary            = SuccessGreen,
    onTertiary          = SurfaceWhite,
    background          = WarmBackground,
    onBackground        = PrimaryText,
    surface             = SurfaceWhite,
    onSurface           = PrimaryText,
    surfaceVariant      = SurfaceContainer,
    onSurfaceVariant    = SecondaryText,
    outline             = OutlineVariant,
    error               = ErrorRed,
    onError             = SurfaceWhite,
)

private val FocusBuddyDarkColorScheme = darkColorScheme(
    primary             = DarkPrimary,
    onPrimary           = DarkBackground,
    primaryContainer    = DarkPrimaryContainer,
    onPrimaryContainer  = DarkTextPrimary,
    secondary           = DarkSecondary,
    onSecondary         = DarkBackground,
    secondaryContainer  = DarkSurfaceVariant,
    onSecondaryContainer = DarkTextPrimary,
    tertiary            = SuccessGreen,
    onTertiary          = DarkBackground,
    background          = DarkBackground,
    onBackground        = DarkTextPrimary,
    surface             = DarkSurface,
    onSurface           = DarkTextPrimary,
    surfaceVariant      = DarkSurfaceVariant,
    onSurfaceVariant    = DarkTextSecondary,
    outline             = DarkOutline,
    error               = ErrorRed,
    onError             = DarkBackground,
)

@Composable
fun FocusBuddyTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) FocusBuddyDarkColorScheme else FocusBuddyColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = FocusBuddyTypography,
        shapes      = FocusBuddyShapes,
        content     = content
    )
}