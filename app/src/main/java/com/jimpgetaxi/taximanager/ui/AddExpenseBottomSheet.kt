package com.jimpgetaxi.taximanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.jimpgetaxi.taximanager.R
import com.jimpgetaxi.taximanager.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseBottomSheet(
    onDismiss: () -> Unit,
    onSave: (amount: String, category: String) -> Unit,
    defaultCategory: String = ""
) {
    var amount by remember { mutableStateOf("") }
    val otherCategoryString = stringResource(R.string.cat_other)
    var selectedCategory by remember { mutableStateOf(defaultCategory) }
    var customCategory by remember { mutableStateOf("") }
    var showCustomInput by remember { mutableStateOf(false) }

    val categories = listOf(
        stringResource(R.string.cat_fuel),
        stringResource(R.string.cat_coffee),
        stringResource(R.string.cat_tolls),
        stringResource(R.string.cat_wash),
        stringResource(R.string.cat_service),
        otherCategoryString
    )

    LaunchedEffect(categories.firstOrNull()) {
        if (selectedCategory.isEmpty() && categories.isNotEmpty()) {
            selectedCategory = categories[0]
        }
    }

    // Determine accent based on category
    val isFuelMode = selectedCategory == stringResource(R.string.cat_fuel)
    val accentColor = if (isFuelMode) GradientFuelStart else NegativeRed
    val accentGradient = if (isFuelMode) {
        Brush.linearGradient(listOf(GradientFuelStart, GradientFuelEnd))
    } else {
        Brush.linearGradient(listOf(GradientExpenseStart, GradientExpenseEnd))
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = BackgroundDark,
        scrimColor = BackgroundDark.copy(alpha = 0.8f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(accentGradient),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isFuelMode) Icons.Filled.LocalGasStation else Icons.Filled.Receipt,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.new_expense_title),
                style = MaterialTheme.typography.titleLarge,
                color = accentColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Amount field with glassmorphism
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphism(20.dp)
                    .padding(4.dp)
            ) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text(stringResource(R.string.amount_eur)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.headlineLarge.copy(
                        color = TextPrimary
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedLabelColor = accentColor,
                        unfocusedLabelColor = TextSecondary,
                        cursorColor = accentColor
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                stringResource(R.string.category_label),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.height(130.dp)
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .then(
                                if (isSelected) {
                                    Modifier.background(accentGradient)
                                } else {
                                    Modifier
                                        .background(CardSurface)
                                        .border(
                                            width = 1.dp,
                                            color = CardBorder,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                }
                            )
                            .clickable {
                                selectedCategory = category
                                showCustomInput = category == otherCategoryString
                            }
                            .padding(vertical = 14.dp, horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
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
                    label = { Text(stringResource(R.string.type_custom_category)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedLabelColor = accentColor,
                        unfocusedLabelColor = TextSecondary,
                        cursorColor = accentColor
                    )
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    val finalCategory = if (selectedCategory == otherCategoryString && customCategory.isNotBlank()) {
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
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.save_expense_btn),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
