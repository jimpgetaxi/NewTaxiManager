package com.jimpgetaxi.taximanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jimpgetaxi.taximanager.R
import com.jimpgetaxi.taximanager.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onNavigateToAddRide: () -> Unit
) {
    val totalRevenue by viewModel.totalRevenue.collectAsState()
    val totalVat by viewModel.totalVat.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val netProfit by viewModel.netProfit.collectAsState()
    val rides by viewModel.rides.collectAsState()

    var showExpenseSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.command_center_title), color = NeonCyan) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CyberBackground
                )
            )
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                FloatingActionButton(
                    onClick = { showExpenseSheet = true },
                    containerColor = NeonPurple,
                    contentColor = CyberBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(stringResource(R.string.add_expense_btn), fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp))
                }
                FloatingActionButton(
                    onClick = onNavigateToAddRide,
                    containerColor = NeonYellow,
                    contentColor = CyberBackground
                ) {
                    Text(stringResource(R.string.add_ride_btn), fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp))
                }
            }
        },
        containerColor = CyberBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Cyberpunk Revenue Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NeonCyanTranslucent, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = GlassSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.total_revenue),
                        color = TextSecondary,
                        fontSize = 14.sp,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = String.format(Locale.US, "%.2f €", totalRevenue),
                        style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = stringResource(R.string.vat_label), color = TextSecondary)
                        Text(
                            text = String.format(Locale.US, "%.2f €", totalVat),
                            color = NeonPurple,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = stringResource(R.string.expenses_label), color = TextSecondary)
                        Text(
                            text = String.format(Locale.US, "%.2f €", totalExpenses),
                            color = NeonPurple,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    HorizontalDivider(
                        color = NeonCyanTranslucent,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = stringResource(R.string.net_profit_label), color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(
                            text = String.format(Locale.US, "%.2f €", netProfit),
                            color = if (netProfit >= 0) NeonCyan else NeonPurple,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.recent_rides),
                color = NeonCyan,
                fontSize = 14.sp,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(rides) { ride ->
                    val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
                    val dateString = sdf.format(Date(ride.timestamp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, NeonPurpleTranslucent, RoundedCornerShape(8.dp))
                            .background(GlassSurface)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = dateString, color = TextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = String.format(Locale.US, "%s %.2f €", stringResource(R.string.actual_label), ride.actualAmount),
                                color = TextPrimary
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = String.format(Locale.US, "%s %.2f €", stringResource(R.string.receipt_label), ride.receiptAmount),
                                color = TextSecondary
                            )
                            Text(
                                text = String.format(Locale.US, "%s %.2f €", stringResource(R.string.vat_short_label), ride.vatAmount),
                                color = NeonPurple,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }

    if (showExpenseSheet) {
        AddExpenseBottomSheet(
            onDismiss = { showExpenseSheet = false },
            onSave = { amount, category ->
                viewModel.addExpense(amount, category)
            }
        )
    }
}
