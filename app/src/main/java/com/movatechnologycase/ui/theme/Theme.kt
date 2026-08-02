package com.movatechnologycase.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Typography

val AppTypography = Typography()

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF3168F4),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDF2FF),
    onPrimaryContainer = Color(0xFF0D1B3E),

    secondary = Color(0xFFDC7A1D),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFF2E5),
    onSecondaryContainer = Color(0xFF6E3700),

    tertiary = Color(0xFF15966B),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE6F7F0),
    onTertiaryContainer = Color(0xFF07533A),

    background = Color(0xFFF5F7FB),
    onBackground = Color(0xFF17213A),

    surface = Color.White,
    onSurface = Color(0xFF17213A),

    surfaceVariant = Color(0xFFECEFF5),
    onSurfaceVariant = Color(0xFF7D879C),

    outline = Color(0xFFB8C0D0),
    outlineVariant = Color(0xFFE7EBF3),

    error = Color(0xFFDC4C64),
    onError = Color.White,
    errorContainer = Color(0xFFFDECEF),
    onErrorContainer = Color(0xFF8A1F33)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9AB2FF),
    onPrimary = Color(0xFF002B72),
    primaryContainer = Color(0xFF1C356A),
    onPrimaryContainer = Color(0xFFDCE5FF),

    secondary = Color(0xFFFFB86C),
    onSecondary = Color(0xFF482900),
    secondaryContainer = Color(0xFF573500),
    onSecondaryContainer = Color(0xFFFFDDB9),

    tertiary = Color(0xFF60D6AA),
    onTertiary = Color(0xFF003827),
    tertiaryContainer = Color(0xFF07513B),
    onTertiaryContainer = Color(0xFF7CF3C5),

    background = Color(0xFF0B1220),
    onBackground = Color(0xFFF1F4FA),

    surface = Color(0xFF121A2B),
    onSurface = Color(0xFFF1F4FA),

    surfaceVariant = Color(0xFF1C2638),
    onSurfaceVariant = Color(0xFFAAB4C7),

    outline = Color(0xFF78849A),
    outlineVariant = Color(0xFF2A354A),

    error = Color(0xFFFF8A9B),
    onError = Color(0xFF5A0015),
    errorContainer = Color(0xFF5A1D2B),
    onErrorContainer = Color(0xFFFFD9DE)
)

@Composable
fun MovaTechnologyCaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        },
        typography = AppTypography,
        content = content
    )
}