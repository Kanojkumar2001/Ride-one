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
    primary = RideOrangePrimary,
    onPrimary = Color.White,
    primaryContainer = RideOrangeLight,
    onPrimaryContainer = RideOrangePrimary,
    secondary = PolishSlate800,
    onSecondary = Color.White,
    tertiary = RideSOSRed,
    onTertiary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = PolishSlate900,
    surface = Color.White,
    onSurface = PolishSlate900,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = PolishSlate700,
    outline = PolishBorder,
    outlineVariant = PolishBorderSubtle,
    error = RideSOSRed,
    onError = Color.White,
    errorContainer = RideSOSRedContainer,
    onErrorContainer = RideSOSRedDark
)

private val DarkColorScheme = lightColorScheme(
    primary = RideOrangePrimary,
    onPrimary = Color.White,
    primaryContainer = RideOrangeLight,
    onPrimaryContainer = RideOrangePrimary,
    secondary = PolishSlate800,
    onSecondary = Color.White,
    tertiary = RideSOSRed,
    onTertiary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = PolishSlate900,
    surface = Color.White,
    onSurface = PolishSlate900,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = PolishSlate700,
    outline = PolishBorder,
    outlineVariant = PolishBorderSubtle,
    error = RideSOSRed,
    onError = Color.White,
    errorContainer = RideSOSRedContainer,
    onErrorContainer = RideSOSRedDark
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
