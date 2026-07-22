package com.jimpgetaxi.taximanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jimpgetaxi.taximanager.R
import com.jimpgetaxi.taximanager.ui.theme.*
import java.util.Locale

@Composable
fun StartShiftDialog(
    initialCostPerKm: Double,
    onDismiss: () -> Unit,
    onConfirm: (odometer: String, costPerKm: String) -> Unit
) {
    var odo by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf(initialCostPerKm.toString()) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = BackgroundDark
        ) {
            Column(
                modifier = Modifier
                    .glassmorphism(28.dp)
                    .padding(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.start_shift_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = PositiveGreen,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = odo,
                    onValueChange = { odo = it },
                    label = { Text(stringResource(R.string.shift_start_odo_label), color = TextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PositiveGreen,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = PositiveGreen
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
                        focusedBorderColor = PositiveGreen,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = PositiveGreen
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
                        colors = ButtonDefaults.buttonColors(containerColor = PositiveGreen),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            stringResource(R.string.confirm_btn),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

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
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = BackgroundDark
        ) {
            Column(
                modifier = Modifier
                    .glassmorphism(28.dp)
                    .padding(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.end_shift_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = NegativeRed,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = endOdoStr,
                    onValueChange = { endOdoStr = it },
                    label = { Text(stringResource(R.string.shift_end_odo_label), color = TextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NegativeRed,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = NegativeRed
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = CardSurface
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(stringResource(R.string.estimated_cost_label), color = TextSecondary)
                        Text(
                            text = String.format(Locale.US, "%.2f €", estimatedCost),
                            color = NegativeRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
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
                        colors = ButtonDefaults.buttonColors(containerColor = NegativeRed),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            stringResource(R.string.confirm_btn),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
