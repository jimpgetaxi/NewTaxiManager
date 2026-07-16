package com.jimpgetaxi.taximanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jimpgetaxi.taximanager.ui.theme.CyberBackground
import com.jimpgetaxi.taximanager.ui.theme.NeonCyan
import com.jimpgetaxi.taximanager.ui.theme.NeonPurple
import com.jimpgetaxi.taximanager.ui.theme.NeonYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseBottomSheet(
    onDismiss: () -> Unit,
    onSave: (amount: String, category: String) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Καύσιμα") }
    var customCategory by remember { mutableStateOf("") }
    var showCustomInput by remember { mutableStateOf(false) }

    val categories = listOf("Καύσιμα", "Καφές", "Διόδια", "Πλύσιμο", "Service", "Άλλο")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CyberBackground,
        scrimColor = CyberBackground.copy(alpha = 0.8f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "NEW EXPENSE",
                color = NeonPurple,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Ποσό (€)", color = NeonCyan) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = NeonCyan.copy(alpha = 0.5f),
                    focusedTextColor = NeonYellow,
                    unfocusedTextColor = NeonYellow
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Κατηγορία:", color = NeonCyan, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(120.dp)
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) NeonPurple else CyberBackground)
                            .clickable {
                                selectedCategory = category
                                showCustomInput = category == "Άλλο"
                            }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) CyberBackground else NeonCyan,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            if (showCustomInput) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = customCategory,
                    onValueChange = { customCategory = it },
                    label = { Text("Πληκτρολόγησε Κατηγορία", color = NeonCyan) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = NeonCyan.copy(alpha = 0.5f),
                        focusedTextColor = NeonYellow,
                        unfocusedTextColor = NeonYellow
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val finalCategory = if (selectedCategory == "Άλλο" && customCategory.isNotBlank()) {
                        customCategory
                    } else {
                        selectedCategory
                    }
                    if (amount.isNotBlank()) {
                        onSave(amount, finalCategory)
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
            ) {
                Text(
                    text = "SAVE EXPENSE",
                    color = CyberBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = 1.5.sp
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
