package com.jimpgetaxi.taximanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jimpgetaxi.taximanager.R
import com.jimpgetaxi.taximanager.ui.theme.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartShiftDialog(
    initialCostPerKm: Double,
    onDismiss: () -> Unit,
    onConfirm: (odometer: String, costPerKm: String) -> Unit
) {
    var odo by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf(initialCostPerKm.toString()) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .neonGlow(NeonYellowTranslucent, cornerRadius = 24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(GlassSurface)
                .border(
                    0.5.dp,
                    Brush.linearGradient(listOf(NeonYellow.copy(alpha=0.5f), Color.Transparent)),
                    RoundedCornerShape(24.dp)
                )
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(R.string.start_shift_title),
                    color = NeonYellow,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = odo,
                    onValueChange = { odo = it },
                    label = { Text(stringResource(R.string.shift_start_odo_label), color = TextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonYellow,
                        unfocusedBorderColor = NeonYellow.copy(alpha = 0.5f),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = cost,
                    onValueChange = { cost = it },
                    label = { Text(stringResource(R.string.shift_cost_km_label), color = TextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonYellow,
                        unfocusedBorderColor = NeonYellow.copy(alpha = 0.5f),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel_btn), color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (odo.isNotBlank() && cost.isNotBlank()) {
                                onConfirm(odo, cost)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonYellow),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.neonGlow(NeonYellow, cornerRadius = 24.dp)
                    ) {
                        Text(stringResource(R.string.confirm_btn), color = CyberBackground, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndShiftDialog(
    startOdo: Double,
    costPerKm: Double,
    onDismiss: () -> Unit,
    onConfirm: (endOdometer: String) -> Unit
) {
    var endOdoStr by remember { mutableStateOf("") }
    
    val endOdo = endOdoStr.replace(",", ".").toDoubleOrNull() ?: 0.0
    val distance = if (endOdo > startOdo) endOdo - startOdo else 0.0
    val estimatedCost = distance * costPerKm

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .neonGlow(NeonPurpleTranslucent, cornerRadius = 24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(GlassSurface)
                .border(
                    0.5.dp,
                    Brush.linearGradient(listOf(NeonPurple.copy(alpha=0.5f), Color.Transparent)),
                    RoundedCornerShape(24.dp)
                )
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(R.string.end_shift_title),
                    color = NeonPurple,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = endOdoStr,
                    onValueChange = { endOdoStr = it },
                    label = { Text(stringResource(R.string.shift_end_odo_label), color = TextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = NeonPurple.copy(alpha = 0.5f),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberBackground)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(R.string.estimated_cost_label), color = TextSecondary)
                    Text(
                        text = String.format(Locale.US, "%.2f €", estimatedCost),
                        color = NeonPurple,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel_btn), color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (endOdoStr.isNotBlank()) {
                                onConfirm(endOdoStr)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.neonGlow(NeonPurple, cornerRadius = 24.dp)
                    ) {
                        Text(stringResource(R.string.confirm_btn), color = CyberBackground, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
