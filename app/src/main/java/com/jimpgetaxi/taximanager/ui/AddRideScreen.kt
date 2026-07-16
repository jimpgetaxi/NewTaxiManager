package com.jimpgetaxi.taximanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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

    // Live VAT calculation
    val receiptVal = receiptAmount.replace(",", ".").toDoubleOrNull() ?: 0.0
    val estimatedVat = (receiptVal / 1.13) * 0.13

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Ride", color = NeonCyan) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = NeonCyan)
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
                label = { Text("Ποσό Είσπραξης (Actual)") },
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
                label = { Text("Ποσό Απόδειξης (Receipt)") },
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

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = CyberSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                PaddingValues(16.dp).let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Υπολογιζόμενος ΦΠΑ (13%):", color = TextSecondary)
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
                        viewModel.addRide(actualAmount, receiptAmount)
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonYellow),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "ΑΠΟΘΗΚΕΥΣΗ (SAVE)",
                    color = CyberBackground,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}
