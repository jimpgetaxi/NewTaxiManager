package com.jimpgetaxi.taximanager.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.neonGlow(
    color: Color,
    cornerRadius: Dp = 24.dp,
    blurRadius: Dp = 20.dp
): Modifier = this.drawWithCache {
    val paint = Paint()
    val frameworkPaint = paint.asFrameworkPaint()
    frameworkPaint.color = android.graphics.Color.TRANSPARENT
    
    val blurRadiusPx = blurRadius.toPx().coerceAtLeast(1f)
    
    frameworkPaint.setShadowLayer(
        blurRadiusPx,
        0f,
        0f,
        color.toArgb()
    )

    onDrawBehind {
        val cornerRadiusPx = cornerRadius.toPx()
        
        drawIntoCanvas { canvas ->
            canvas.drawRoundRect(
                left = 0f,
                top = 0f,
                right = size.width,
                bottom = size.height,
                radiusX = cornerRadiusPx,
                radiusY = cornerRadiusPx,
                paint = paint
            )
        }
    }
}
