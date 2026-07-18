package com.jimpgetaxi.taximanager.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
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
                title = { Text(stringResource(R.string.new_ride_title), color = NeonCyan) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonCyan)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CyberBackground
                )
            )
        },
        containerColor = CyberBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = actualAmount,
                onValueChange = { actualAmount = it },
                label = { Text(stringResource(R.string.actual_amount_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonYellow,
                    unfocusedBorderColor = TextSecondary,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedLabelColor = NeonYellow,
                    unfocusedLabelColor = TextSecondary
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
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = TextSecondary,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedLabelColor = NeonCyan,
                    unfocusedLabelColor = TextSecondary
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
                    focusedBorderColor = NeonPurple,
                    unfocusedBorderColor = TextSecondary,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedLabelColor = NeonPurple,
                    unfocusedLabelColor = TextSecondary
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .neonGlow(NeonCyanTranslucent, cornerRadius = 24.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(GlassSurface)
                    .border(
                        1.dp,
                        Brush.linearGradient(listOf(NeonCyan, NeonPurple)),
                        RoundedCornerShape(24.dp)
                    )
            ) {
                PaddingValues(16.dp).let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(stringResource(R.string.vat_label), color = TextSecondary)
                        Text(
                            text = String.format(Locale.US, "%.2f €", estimatedVat),
                            color = NeonPurple,
                            fontWeight = FontWeight.Bold
                        )
                    }
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
                    .height(56.dp)
                    .neonGlow(NeonYellow, cornerRadius = 24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonYellow),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.save_ride_btn),
                    color = Color.Black,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}
