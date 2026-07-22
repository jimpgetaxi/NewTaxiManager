package com.jimpgetaxi.taximanager.ui.components

import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.jimpgetaxi.taximanager.ui.theme.*

@Composable
fun AmbientBackground(modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .fillMaxSize()
        .background(BackgroundDark)) {
        
        val blurModifier = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Modifier.blur(100.dp)
        } else {
            Modifier
        }
        
        Canvas(modifier = Modifier.fillMaxSize().then(blurModifier)) {
            // Teal top-left
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(GlowTeal, GlowTeal.copy(alpha = 0f)),
                    center = Offset(0f, 0f),
                    radius = 340.dp.toPx()
                ),
                radius = 340.dp.toPx(),
                center = Offset(0f, 0f)
            )
            
            // Purple bottom-right
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(GlowPurple, GlowPurple.copy(alpha = 0f)),
                    center = Offset(size.width, size.height),
                    radius = 340.dp.toPx()
                ),
                radius = 340.dp.toPx(),
                center = Offset(size.width, size.height)
            )
        }
    }
}
