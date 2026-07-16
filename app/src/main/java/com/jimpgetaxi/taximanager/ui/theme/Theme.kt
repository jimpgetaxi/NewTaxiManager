package com.jimpgetaxi.taximanager.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CyberpunkColorScheme = darkColorScheme(
    primary = NeonCyan,
    secondary = NeonPurple,
    tertiary = NeonYellow,
    background = CyberBackground,
    surface = CyberSurface,
    onPrimary = CyberBackground,
    onSecondary = TextPrimary,
    onTertiary = CyberBackground,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun TaxiManagerTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = CyberpunkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
