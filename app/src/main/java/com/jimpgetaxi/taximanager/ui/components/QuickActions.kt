package com.jimpgetaxi.taximanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jimpgetaxi.taximanager.ui.theme.*

@Composable
fun QuickActions(
    onIncomeClick: () -> Unit,
    onExpenseClick: () -> Unit,
    onFuelClick: () -> Unit,
    onShiftClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        QuickActionItem(
            label = "Έσοδο",
            icon = Icons.Filled.Add,
            gradientColors = listOf(GradientIncomeStart, GradientIncomeEnd),
            onClick = onIncomeClick
        )
        QuickActionItem(
            label = "Έξοδο",
            icon = Icons.Filled.Remove,
            gradientColors = listOf(GradientExpenseStart, GradientExpenseEnd),
            onClick = onExpenseClick
        )
        QuickActionItem(
            label = "Καύσιμα",
            icon = Icons.Filled.LocalGasStation,
            gradientColors = listOf(GradientFuelStart, GradientFuelEnd),
            onClick = onFuelClick
        )
        QuickActionItem(
            label = "Βάρδια",
            icon = Icons.Filled.AccessTime,
            gradientColors = listOf(GradientShiftStart, GradientShiftEnd),
            onClick = onShiftClick
        )
    }
}

@Composable
private fun QuickActionItem(
    label: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(gradientColors))
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
