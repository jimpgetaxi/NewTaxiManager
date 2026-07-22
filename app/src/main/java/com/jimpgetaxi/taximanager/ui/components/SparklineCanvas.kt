package com.jimpgetaxi.taximanager.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun SparklineCanvas(
    data: List<Float>,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val maxData = data.maxOrNull() ?: 1f
        val minData = data.minOrNull() ?: 0f
        val range = maxData - minData
        val yFactor = if (range == 0f) 0f else height / range

        val stepX = width / (data.size - 1).coerceAtLeast(1)

        val path = Path()
        val points = mutableListOf<Offset>()

        data.forEachIndexed { index, value ->
            val x = index * stepX
            val y = height - ((value - minData) * yFactor)
            points.add(Offset(x, y))
        }

        if (points.isNotEmpty()) {
            path.moveTo(points.first().x, points.first().y)
            for (i in 0 until points.size - 1) {
                val p1 = points[i]
                val p2 = points[i + 1]
                val cx1 = (p1.x + p2.x) / 2
                val cy1 = p1.y
                val cx2 = (p1.x + p2.x) / 2
                val cy2 = p2.y
                path.cubicTo(cx1, cy1, cx2, cy2, p2.x, p2.y)
            }

            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = 4f)
            )

            // Optional fill area
            val fillPath = Path().apply {
                addPath(path)
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(lineColor.copy(alpha = 0.3f), Color.Transparent)
                )
            )
        }
    }
}
