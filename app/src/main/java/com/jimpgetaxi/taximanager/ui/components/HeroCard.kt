package com.jimpgetaxi.taximanager.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jimpgetaxi.taximanager.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HeroCard(
    greeting: String,
    income: Double,
    trendPercent: Double,
    sparklineData: List<Float>,
    isShiftActive: Boolean,
    incomeLabel: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .glassmorphism(cornerRadius = 28.dp)
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = greeting,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = incomeLabel,
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            val formattedIncome = NumberFormat.getCurrencyInstance(Locale("el", "GR")).format(income)
            Text(
                text = formattedIncome,
                style = MaterialTheme.typography.displayLarge
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            val isPositive = trendPercent >= 0
            val trendText = if (isPositive) "▲ +$trendPercent%" else "▼ $trendPercent%"
            val trendColor = if (isPositive) PositiveGreen else NegativeRed
            
            Text(
                text = trendText,
                style = MaterialTheme.typography.bodyLarge,
                color = trendColor
            )
        }
        
        SparklineCanvas(
            data = sparklineData,
            lineColor = BrandAccent,
            modifier = Modifier
                .width(100.dp)
                .height(60.dp)
        )
    }
}
