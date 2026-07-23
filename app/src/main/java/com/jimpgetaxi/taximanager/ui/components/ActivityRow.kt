package com.jimpgetaxi.taximanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jimpgetaxi.taximanager.ui.theme.*
import com.jimpgetaxi.taximanager.ui.ActivityItem
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ActivityRow(
    item: ActivityItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconInfo = when (item.category.lowercase(Locale.getDefault())) {
            "ride" -> Pair(Icons.Filled.DirectionsCar, listOf(GradientIncomeStart, GradientIncomeEnd))
            "καύσιμα" -> Pair(Icons.Filled.LocalGasStation, listOf(GradientFuelStart, GradientFuelEnd))
            else -> Pair(Icons.Filled.Receipt, listOf(GradientExpenseStart, GradientExpenseEnd))
        }
        
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Brush.linearGradient(iconInfo.second)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconInfo.first,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary
            )
        }
        
        Column(horizontalAlignment = Alignment.End) {
            val amountStr = NumberFormat.getCurrencyInstance(Locale("el", "GR")).format(item.amount)
            val amountText = if (item.isIncome) "+$amountStr" else "-$amountStr"
            val amountColor = if (item.isIncome) PositiveGreen else NegativeRed
            
            Text(
                text = amountText,
                style = MaterialTheme.typography.bodyLarge,
                color = amountColor
            )
            
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            Text(
                text = sdf.format(Date(item.timestamp)),
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary
            )
        }
    }
}
