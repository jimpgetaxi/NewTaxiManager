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
import com.jimpgetaxi.taximanager.ui.components.DateTimePickerRow
import com.jimpgetaxi.taximanager.ui.theme.*
import java.util.Locale

private val FieldShape = RoundedCornerShape(20.dp)

@Composable
fun StartShiftDialog(
    initialCostPerKm: Double,
    initialWearFund: Double,
    onDismiss: () -> Unit,
    onConfirm: (odometer: String, costPerKm: String, fund: String, timestamp: Long) -> Unit
) {
    var odo by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf(initialCostPerKm.toString()) }
    var fund by remember { mutableStateOf(String.format(Locale.US, "%.2f", initialWearFund)) }
    var timestamp by remember { mutableStateOf(System.currentTimeMillis()) }

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
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Συμπλήρωσε τα στοιχεία εκκίνησης",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextTertiary
                )
                Spacer(modifier = Modifier.height(20.dp))

                DateTimePickerRow(
                    timestamp = timestamp,
                    onTimestampChanged = { timestamp = it },
                    accentColor = PositiveGreen
                )
                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = odo,
                    onValueChange = { odo = it },
                    label = { Text(stringResource(R.string.shift_start_odo_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = FieldShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PositiveGreen,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedLabelColor = PositiveGreen,
                        unfocusedLabelColor = TextSecondary,
                        cursorColor = PositiveGreen,
                        focusedContainerColor = CardSurface,
                        unfocusedContainerColor = CardSurface
                    )
                )
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = cost,
                    onValueChange = { cost = it },
                    label = { Text(stringResource(R.string.shift_cost_km_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    shape = FieldShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PositiveGreen,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedLabelColor = PositiveGreen,
                        unfocusedLabelColor = TextSecondary,
                        cursorColor = PositiveGreen,
                        focusedContainerColor = CardSurface,
                        unfocusedContainerColor = CardSurface
                    )
                )
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = fund,
                    onValueChange = { fund = it },
                    label = { Text("Τρέχον Ταμείο Φθοράς (€)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    shape = FieldShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PositiveGreen,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedLabelColor = PositiveGreen,
                        unfocusedLabelColor = TextSecondary,
                        cursorColor = PositiveGreen,
                        focusedContainerColor = CardSurface,
                        unfocusedContainerColor = CardSurface
                    )
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
                            if (odo.isNotBlank() && cost.isNotBlank() && fund.isNotBlank()) {
                                onConfirm(odo, cost, fund, timestamp)
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
    onConfirm: (endOdometer: String, timestamp: Long) -> Unit
) {
    var endOdoStr by remember { mutableStateOf("") }
    var timestamp by remember { mutableStateOf(System.currentTimeMillis()) }
    
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
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Συμπλήρωσε τον τελικό χιλιομετρητή",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextTertiary
                )
                Spacer(modifier = Modifier.height(20.dp))

                DateTimePickerRow(
                    timestamp = timestamp,
                    onTimestampChanged = { timestamp = it },
                    accentColor = NegativeRed
                )
                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = endOdoStr,
                    onValueChange = { endOdoStr = it },
                    label = { Text(stringResource(R.string.shift_end_odo_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = FieldShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NegativeRed,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedLabelColor = NegativeRed,
                        unfocusedLabelColor = TextSecondary,
                        cursorColor = NegativeRed,
                        focusedContainerColor = CardSurface,
                        unfocusedContainerColor = CardSurface
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Estimated Cost Preview
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassmorphism(16.dp)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(R.string.estimated_cost_label),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )
                    Text(
                        text = String.format(Locale.US, "%.2f €", estimatedCost),
                        style = MaterialTheme.typography.titleLarge,
                        color = NegativeRed
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
                                onConfirm(endOdoStr, timestamp)
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
