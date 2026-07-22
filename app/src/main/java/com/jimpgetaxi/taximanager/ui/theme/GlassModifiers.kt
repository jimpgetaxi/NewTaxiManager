package com.jimpgetaxi.taximanager.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Custom Glassmorphism modifier for the ShiftGuard design system.
 * Applies: CardSurface background + 1dp CardBorder gradient stroke + strict corner radius.
 * NO Material elevation or shadows.
 */
fun Modifier.glassmorphism(
    cornerRadius: Dp = 28.dp
): Modifier = this
    .clip(RoundedCornerShape(cornerRadius))
    .background(CardSurface)
    .border(
        width = 1.dp,
        brush = Brush.linearGradient(
            colors = listOf(
                CardBorder,
                Color.Transparent,
                CardBorder.copy(alpha = 0.1f)
            )
        ),
        shape = RoundedCornerShape(cornerRadius)
    )

/**
 * Glassmorphism with custom background color.
 */
fun Modifier.glassmorphism(
    cornerRadius: Dp = 28.dp,
    backgroundColor: Color
): Modifier = this
    .clip(RoundedCornerShape(cornerRadius))
    .background(backgroundColor)
    .border(
        width = 1.dp,
        brush = Brush.linearGradient(
            colors = listOf(
                CardBorder,
                Color.Transparent,
                CardBorder.copy(alpha = 0.1f)
            )
        ),
        shape = RoundedCornerShape(cornerRadius)
    )
