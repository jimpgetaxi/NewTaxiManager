package com.jimpgetaxi.taximanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jimpgetaxi.taximanager.R
import com.jimpgetaxi.taximanager.ui.theme.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRideScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var actualAmount by remember { mutableStateOf("") }
    var receiptAmount by remember { mutableStateOf("") }
    var currentOdometer by remember { mutableStateOf("") }

    // Live VAT calculation
    val receiptVal = receiptAmount.replace(",", ".").toDoubleOrNull() ?: 0.0
    val estimatedVat = (receiptVal / 1.13) * 0.13

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.new_ride_title), color = BrandAccent) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BrandAccent)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = BackgroundDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            OutlinedTextField(
                value = actualAmount,
                onValueChange = { actualAmount = it },
                label = { Text(stringResource(R.string.actual_amount_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PositiveGreen,
                    unfocusedBorderColor = CardBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedLabelColor = PositiveGreen,
                    unfocusedLabelColor = TextSecondary,
                    cursorColor = PositiveGreen
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = receiptAmount,
                onValueChange = { receiptAmount = it },
                label = { Text(stringResource(R.string.receipt_amount_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandAccent,
                    unfocusedBorderColor = CardBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedLabelColor = BrandAccent,
                    unfocusedLabelColor = TextSecondary,
                    cursorColor = BrandAccent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = currentOdometer,
                onValueChange = { currentOdometer = it },
                label = { Text(stringResource(R.string.optional_odometer_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GradientShiftStart,
                    unfocusedBorderColor = CardBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedLabelColor = GradientShiftStart,
                    unfocusedLabelColor = TextSecondary,
                    cursorColor = GradientShiftStart
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // VAT Preview Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = CardSurface
            ) {
                Row(
                    modifier = Modifier
                        .glassmorphism(20.dp)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(R.string.vat_label), color = TextSecondary)
                    Text(
                        text = String.format(Locale.US, "%.2f €", estimatedVat),
                        color = NegativeRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (actualAmount.isNotBlank() && receiptAmount.isNotBlank()) {
                        viewModel.addRide(actualAmount, receiptAmount, currentOdometer)
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PositiveGreen),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.save_ride_btn),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
