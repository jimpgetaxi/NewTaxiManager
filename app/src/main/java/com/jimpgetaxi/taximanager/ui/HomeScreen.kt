package com.jimpgetaxi.taximanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jimpgetaxi.taximanager.R
import com.jimpgetaxi.taximanager.ui.components.*
import com.jimpgetaxi.taximanager.ui.theme.*
import java.util.Calendar

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToAddRide: () -> Unit
) {
    val totalRevenue by viewModel.totalRevenue.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val isShiftActive by viewModel.isShiftActive.collectAsState()
    val startOdometer by viewModel.startOdometer.collectAsState()
    val costPerKm by viewModel.costPerKm.collectAsState()
    val recentActivity by viewModel.recentActivity.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val onboardingDone by viewModel.onboardingDone.collectAsState()

    var showExpenseSheet by remember { mutableStateOf(false) }
    var expenseDefaultCategory by remember { mutableStateOf("") }
    var showStartShiftDialog by remember { mutableStateOf(false) }
    var showEndShiftDialog by remember { mutableStateOf(false) }
    var showNameDialog by remember { mutableStateOf(false) }

    // Show name dialog ONLY on confirmed first launch (onboardingDone = false from DataStore)
    LaunchedEffect(onboardingDone) {
        if (!onboardingDone) {
            showNameDialog = true
        }
    }

    // Dynamic greeting based on time of day
    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when {
            hour < 12 -> "greeting_morning"
            hour < 17 -> "greeting_afternoon"
            else -> "greeting_evening"
        }
    }
    val greetingText = when (greeting) {
        "greeting_morning" -> stringResource(R.string.greeting_morning)
        "greeting_afternoon" -> stringResource(R.string.greeting_afternoon)
        else -> stringResource(R.string.greeting_evening)
    }
    val fullGreeting = if (userName.isNotEmpty()) "$greetingText, $userName" else greetingText

    val incomeLabel = if (isShiftActive) {
        stringResource(R.string.shift_income_label)
    } else {
        stringResource(R.string.monthly_income_label)
    }

    // Trend placeholder (mock for now)
    val trendPercent = if (totalRevenue > 0) 12.0 else 0.0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.command_center_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                // Avatar placeholder
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = CardSurface
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = if (userName.isNotEmpty()) userName.first().uppercase() else "?",
                            style = MaterialTheme.typography.bodyLarge,
                            color = BrandAccent
                        )
                    }
                }
            }
        }

        // Hero Card
        item {
            HeroCard(
                greeting = fullGreeting,
                incomeLabel = incomeLabel,
                income = totalRevenue,
                trendPercent = trendPercent,
                sparklineData = viewModel.incomeSparkline,
                isShiftActive = isShiftActive
            )
        }

        // Quick Actions
        item {
            val fuelCategoryName = stringResource(R.string.cat_fuel)
            QuickActions(
                isShiftActive = isShiftActive,
                onIncomeClick = onNavigateToAddRide,
                onExpenseClick = {
                    expenseDefaultCategory = ""
                    showExpenseSheet = true
                },
                onFuelClick = {
                    expenseDefaultCategory = fuelCategoryName
                    showExpenseSheet = true
                },
                onShiftClick = {
                    if (isShiftActive) showEndShiftDialog = true
                    else showStartShiftDialog = true
                }
            )
        }

        // Summary Cards
        item {
            SummaryCards(
                income = totalRevenue,
                expenses = totalExpenses,
                incomeTrend = 12.0,
                expenseTrend = -8.0,
                incomeSparkline = viewModel.incomeSparkline,
                expenseSparkline = viewModel.expenseSparkline
            )
        }

        // Performance Chart
        item {
            PerformanceChart(
                incomeData = viewModel.incomeChartData,
                expenseData = viewModel.expenseChartData
            )
        }

        // Recent Activity Header
        item {
            Text(
                text = stringResource(R.string.recent_activity_title),
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )
        }

        // Activity Rows
        items(recentActivity, key = { it.id }) { item ->
            ActivityRow(item = item)
        }

        // Bottom spacing for floating nav bar
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    // Dialogs & Bottom Sheets
    if (showExpenseSheet) {
        AddExpenseBottomSheet(
            onDismiss = { showExpenseSheet = false },
            onSave = { amount, category, timestamp ->
                viewModel.addExpense(amount, category, timestamp)
            },
            defaultCategory = expenseDefaultCategory
        )
    }

    if (showStartShiftDialog) {
        StartShiftDialog(
            initialCostPerKm = costPerKm,
            onDismiss = { showStartShiftDialog = false },
            onConfirm = { odo, cost, timestamp ->
                viewModel.startShift(odo, cost, timestamp)
                showStartShiftDialog = false
            }
        )
    }

    if (showEndShiftDialog) {
        val expenseCategoryName = stringResource(R.string.vehicle_maintenance_category)
        EndShiftDialog(
            startOdo = startOdometer,
            costPerKm = costPerKm,
            onDismiss = { showEndShiftDialog = false },
            onConfirm = { endOdo, timestamp ->
                viewModel.endShift(endOdo, expenseCategoryName, timestamp)
                showEndShiftDialog = false
            }
        )
    }

    // Welcome Dialog (first launch)
    if (showNameDialog) {
        var nameInput by remember { mutableStateOf("") }
        Dialog(onDismissRequest = { /* Cannot dismiss */ }) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = BackgroundDark
            ) {
                Column(
                    modifier = Modifier
                        .glassmorphism(28.dp)
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.welcome_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = BrandAccent,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.welcome_message),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text(stringResource(R.string.name_hint), color = TextTertiary) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandAccent,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = BrandAccent
                        )
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            if (nameInput.isNotBlank()) {
                                viewModel.saveUserName(nameInput.trim())
                                showNameDialog = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandAccent),
                        shape = RoundedCornerShape(16.dp),
                        enabled = nameInput.isNotBlank()
                    ) {
                        Text(
                            text = stringResource(R.string.save_btn),
                            color = BackgroundDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
