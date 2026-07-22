package com.jimpgetaxi.taximanager.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jimpgetaxi.taximanager.ui.theme.*

@Composable
fun PerformanceChart(
    incomeData: List<Float>,
    expenseData: List<Float>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .glassmorphism(cornerRadius = 28.dp)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Τελευταίες 7 Ημέρες",
                style = MaterialTheme.typography.titleLarge
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LegendItem(color = PositiveGreen, label = "Έσοδα")
                LegendItem(color = NegativeRed, label = "Έξοδα")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        Canvas(modifier = Modifier.fillMaxWidth().height(150.dp)) {
            val width = size.width
            val height = size.height
            val maxData = (incomeData + expenseData).maxOrNull() ?: 1f
            val minData = (incomeData + expenseData).minOrNull() ?: 0f
            val range = maxData - minData
            val yFactor = if (range == 0f) 0f else height / range
            val stepX = width / 6f

            fun drawCurve(data: List<Float>, color: Color) {
                if (data.isEmpty()) return
                val path = Path()
                val points = data.mapIndexed { index, value ->
                    Offset(index * stepX, height - ((value - minData) * yFactor))
                }
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
                drawPath(path = path, color = color, style = Stroke(width = 4f))
                
                val fillPath = Path().apply {
                    addPath(path)
                    lineTo(width, height)
                    lineTo(0f, height)
                    close()
                }
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(color.copy(alpha = 0.3f), Color.Transparent)
                    )
                )
            }
            
            drawCurve(incomeData, PositiveGreen)
            drawCurve(expenseData, NegativeRed)
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val days = listOf("Δ", "Τ", "Τ", "Π", "Π", "Σ", "Κ")
            days.forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary
                )
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(8.dp)) {
            drawCircle(color = color)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
    }
}
