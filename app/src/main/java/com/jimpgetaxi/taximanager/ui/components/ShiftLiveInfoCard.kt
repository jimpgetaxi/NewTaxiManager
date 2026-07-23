package com.jimpgetaxi.taximanager.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jimpgetaxi.taximanager.R
import com.jimpgetaxi.taximanager.ui.theme.*
import java.util.Locale
import kotlin.math.max

@Composable
fun ShiftLiveInfoCard(
    startOdo: Double,
    currentOdo: Double,
    vehicleCost: Double,
    modifier: Modifier = Modifier
) {
    val distance = max(0.0, currentOdo - startOdo)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .glassmorphism(24.dp)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Speed,
                contentDescription = null,
                tint = BrandAccent,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Τρέχουσα Βάρδια",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoColumn(label = "Αρχικά", value = String.format(Locale.US, "%.1f", startOdo))
            InfoColumn(label = "Τρέχοντα", value = String.format(Locale.US, "%.1f", currentOdo))
            InfoColumn(label = "Διανυθέντα", value = String.format(Locale.US, "%.1f χλμ", distance), highlight = true)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .glassmorphism(16.dp)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.DirectionsCar,
                    contentDescription = null,
                    tint = NegativeRed,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Κόστος Οχήματος",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
            }
            Text(
                text = String.format(Locale.US, "%.2f €", vehicleCost),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = NegativeRed
            )
        }
    }
}

@Composable
private fun InfoColumn(label: String, value: String, highlight: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
            color = if (highlight) BrandAccent else TextPrimary
        )
    }
}
