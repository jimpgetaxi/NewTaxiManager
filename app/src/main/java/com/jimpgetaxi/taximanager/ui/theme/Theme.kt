package com.jimpgetaxi.taximanager.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ShiftGuardColorScheme = darkColorScheme(
    primary = BrandAccent,
    secondary = PositiveGreen,
    tertiary = GradientFuelStart,
    background = BackgroundDark,
    surface = BackgroundDark,
    surfaceVariant = CardSurface,
    onPrimary = BackgroundDark,
    onSecondary = TextPrimary,
    onTertiary = BackgroundDark,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = NegativeRed,
    onError = TextPrimary,
    outline = CardBorder
)

@Composable
fun TaxiManagerTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            @Suppress("DEPRECATION")
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            @Suppress("DEPRECATION")
            window.navigationBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = ShiftGuardColorScheme,
        typography = ShiftGuardTypography,
        content = content
    )
}
