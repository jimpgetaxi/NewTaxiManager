package com.jimpgetaxi.taximanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
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
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Header icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(GradientIncomeStart, GradientIncomeEnd))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.DirectionsCar,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.new_ride_title),
                style = MaterialTheme.typography.titleLarge,
                color = PositiveGreen,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Amount fields inside glassmorphism card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphism(24.dp)
                    .padding(16.dp)
            ) {
                Column {
                    // Actual Amount
                    OutlinedTextField(
                        value = actualAmount,
                        onValueChange = { actualAmount = it },
                        label = { Text(stringResource(R.string.actual_amount_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.headlineLarge.copy(color = TextPrimary),
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

                    // Receipt Amount
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
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Odometer (separate card)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphism(24.dp)
                    .padding(16.dp)
            ) {
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
            }

            Spacer(modifier = Modifier.height(20.dp))

            // VAT Preview Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphism(20.dp)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.vat_label),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
                Text(
                    text = String.format(Locale.US, "%.2f €", estimatedVat),
                    style = MaterialTheme.typography.headlineLarge,
                    color = NegativeRed,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Save button
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
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
