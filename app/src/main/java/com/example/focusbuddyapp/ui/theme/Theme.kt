package com.example.focusbuddyapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
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

@Composable
fun FocusBuddyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FocusBuddyColorScheme,
        typography  = FocusBuddyTypography,
        shapes      = FocusBuddyShapes,
        content     = content
    )
}