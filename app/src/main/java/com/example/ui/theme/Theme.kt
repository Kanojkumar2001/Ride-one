package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = RideDeepNavy,
    onPrimary = Color.White,
    primaryContainer = RideNavySurface,
    onPrimaryContainer = Color.White,
    secondary = RideElectricLime,
    onSecondary = RideDeepNavy,
    secondaryContainer = RideLimeContainer,
    onSecondaryContainer = RideLimeOnContainer,
    tertiary = RideSOSRed,
    onTertiary = Color.White,
    background = RideBackgroundLight,
    onBackground = RideTextPrimary,
    surface = RideSurfaceLight,
    onSurface = RideTextPrimary,
    surfaceVariant = RideSurfaceVariant,
    onSurfaceVariant = RideTextSecondary,
    outline = RideBorderLight,
    outlineVariant = RideBorderSubtle,
    error = RideSOSRed,
    onError = Color.White,
    errorContainer = RideSOSRedContainer,
    onErrorContainer = RideSOSRedDark
)

private val DarkColorScheme = darkColorScheme(
    primary = RideElectricLime,
    onPrimary = RideDeepNavy,
    primaryContainer = RideNavyElevated,
    onPrimaryContainer = Color.White,
    secondary = RideElectricLime,
    onSecondary = RideDeepNavy,
    tertiary = RideSOSRed,
    onTertiary = Color.White,
    background = RideNavyDark,
    onBackground = Color.White,
    surface = RideDeepNavy,
    onSurface = Color.White,
    surfaceVariant = RideNavySurface,
    onSurfaceVariant = RideTextMuted,
    outline = RideNavyElevated,
    error = RideSOSRed,
    onError = Color.White
)

val RideShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),   // rounded-2xl
    large = RoundedCornerShape(24.dp),    // rounded-3xl
    extraLarge = RoundedCornerShape(32.dp) // rounded-[2rem]
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep intentional Ride One brand identity
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = RideShapes,
        content = content
    )
}
