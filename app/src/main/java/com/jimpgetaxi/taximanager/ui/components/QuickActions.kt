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
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.jimpgetaxi.taximanager.ui.theme.*

@Composable
fun QuickActions(
    isShiftActive: Boolean,
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
            onClick = onIncomeClick,
            enabled = isShiftActive
        )
        QuickActionItem(
            label = "Έξοδο",
            icon = Icons.Filled.Remove,
            gradientColors = listOf(GradientExpenseStart, GradientExpenseEnd),
            onClick = onExpenseClick,
            enabled = isShiftActive
        )
        QuickActionItem(
            label = "Καύσιμα",
            icon = Icons.Filled.LocalGasStation,
            gradientColors = listOf(GradientFuelStart, GradientFuelEnd),
            onClick = onFuelClick,
            enabled = isShiftActive
        )
        QuickActionItem(
            label = "Βάρδια",
            icon = Icons.Filled.AccessTime,
            gradientColors = listOf(GradientShiftStart, GradientShiftEnd),
            onClick = onShiftClick,
            enabled = true // Shift button is always enabled
        )
    }
}

@Composable
private fun QuickActionItem(
    label: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    onClick: () -> Unit,
    enabled: Boolean
) {
    val context = LocalContext.current
    val alpha = if (enabled) 1f else 0.4f
    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(gradientColors).apply { })
                .clickable {
                    if (enabled) {
                        onClick()
                    } else {
                        Toast.makeText(context, "Ξεκινήστε βάρδια πρώτα!", Toast.LENGTH_SHORT).show()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White.copy(alpha = alpha),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = TextPrimary.copy(alpha = alpha)
        )
    }
}
