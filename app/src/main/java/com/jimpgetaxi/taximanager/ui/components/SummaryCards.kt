package com.jimpgetaxi.taximanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jimpgetaxi.taximanager.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SummaryCards(
    income: Double,
    expenses: Double,
    incomeTrend: Double,
    expenseTrend: Double,
    incomeSparkline: List<Float>,
    expenseSparkline: List<Float>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryCard(
            title = "Έσοδα",
            amount = income,
            trend = incomeTrend,
            sparkline = incomeSparkline,
            icon = Icons.Filled.ArrowUpward,
            iconColor = PositiveGreen,
            isIncome = true,
            modifier = Modifier.weight(1f)
        )
        
        SummaryCard(
            title = "Έξοδα",
            amount = expenses,
            trend = expenseTrend,
            sparkline = expenseSparkline,
            icon = Icons.Filled.ArrowDownward,
            iconColor = NegativeRed,
            isIncome = false,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SummaryCard(
    title: String,
    amount: Double,
    trend: Double,
    sparkline: List<Float>,
    icon: ImageVector,
    iconColor: Color,
    isIncome: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .glassmorphism(cornerRadius = 20.dp)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
        
        val formattedAmount = NumberFormat.getCurrencyInstance(Locale("el", "GR")).format(amount)
        Text(
            text = formattedAmount,
            style = MaterialTheme.typography.headlineLarge
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isPositive = trend >= 0
            val trendText = if (isPositive) "+$trend%" else "$trend%"
            val trendColor = if (isIncome) {
                if (isPositive) PositiveGreen else NegativeRed
            } else {
                if (isPositive) NegativeRed else PositiveGreen
            }
            
            Text(
                text = trendText,
                style = MaterialTheme.typography.bodySmall,
                color = trendColor
            )
            
            SparklineCanvas(
                data = sparkline,
                lineColor = iconColor,
                modifier = Modifier
                    .width(40.dp)
                    .height(20.dp)
            )
        }
    }
}

@Composable
fun WearFundCard(
    fund: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .glassmorphism(cornerRadius = 20.dp)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BrandAccent.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowUpward, // Just a placeholder, we can use a generic icon later
                    contentDescription = null,
                    tint = BrandAccent,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Ταμείο Φθοράς",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
                Text(
                    text = "Τρέχον Διαθέσιμο",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary
                )
            }
        }
        
        val formattedFund = NumberFormat.getCurrencyInstance(Locale("el", "GR")).format(fund)
        Text(
            text = formattedFund,
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )
    }
}
