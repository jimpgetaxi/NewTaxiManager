package com.jimpgetaxi.taximanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimpgetaxi.taximanager.data.Expense
import com.jimpgetaxi.taximanager.data.Ride
import com.jimpgetaxi.taximanager.data.RideRepository
import com.jimpgetaxi.taximanager.data.Shift
import com.jimpgetaxi.taximanager.data.ShiftManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

// === MVI State Definitions ===

data class ActivityItem(
    val id: Int,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val isIncome: Boolean,
    val timestamp: Long,
    val category: String
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RideRepository,
    private val shiftManager: ShiftManager
) : ViewModel() {

    private fun getStartOfMonthTimestamp(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private val filterTimestampFlow: Flow<Long> = combine(
        shiftManager.isShiftActiveFlow,
        shiftManager.shiftStartTimeFlow
    ) { active, shiftStart ->
        if (active) shiftStart else getStartOfMonthTimestamp()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val rides: StateFlow<List<Ride>> = filterTimestampFlow
        .flatMapLatest { since -> repository.getRidesSince(since) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val expenses: StateFlow<List<Expense>> = filterTimestampFlow
        .flatMapLatest { since -> repository.getExpensesSince(since) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val totalRevenue: StateFlow<Double> = filterTimestampFlow
        .flatMapLatest { since -> repository.getTotalRevenueSince(since) }
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val totalVat: StateFlow<Double> = filterTimestampFlow
        .flatMapLatest { since -> repository.getTotalVatSince(since) }
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val dbExpensesFlow: Flow<Double> = filterTimestampFlow
        .flatMapLatest { since -> repository.getTotalExpensesSince(since) }
        .map { it ?: 0.0 }

    val isShiftActive: StateFlow<Boolean> = shiftManager.isShiftActiveFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val activeShiftId: StateFlow<Int?> = shiftManager.activeShiftIdFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val startOdometer: StateFlow<Double> = shiftManager.startOdometerFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val costPerKm: StateFlow<Double> = shiftManager.costPerKmFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.22)

    val currentOdometer: StateFlow<Double> = shiftManager.currentOdometerFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val userName: StateFlow<String> = shiftManager.userNameFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val onboardingDone: StateFlow<Boolean> = shiftManager.onboardingDoneFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true) // Default true to prevent flash

    val liveVehicleCost: StateFlow<Double> = combine(isShiftActive, startOdometer, currentOdometer, costPerKm) { active, start, current, cost ->
        if (active && current > start) {
            val rawCost = (current - start) * cost
            kotlin.math.round(rawCost * 10) / 10.0
        } else {
            0.0
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalExpenses: StateFlow<Double> = combine(dbExpensesFlow, liveVehicleCost) { dbExp, liveCost ->
        dbExp + liveCost
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val netProfit: StateFlow<Double> = combine(totalRevenue, totalVat, totalExpenses) { rev, vat, exp ->
        rev - vat - exp
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // === Combined Activity Feed ===
    val recentActivity: StateFlow<List<ActivityItem>> = combine(
        rides,
        expenses
    ) { rideList, expenseList ->
        val rideItems = rideList.map { ride ->
            ActivityItem(
                id = ride.id,
                title = "Κούρσα",
                subtitle = String.format("%.2f € απόδειξη", ride.receiptAmount),
                amount = ride.actualAmount,
                isIncome = true,
                timestamp = ride.timestamp,
                category = "ride"
            )
        }
        val expenseItems = expenseList.map { expense ->
            ActivityItem(
                id = expense.id + 100000,
                title = expense.category,
                subtitle = "",
                amount = expense.amount,
                isIncome = false,
                timestamp = expense.timestamp,
                category = expense.category.lowercase()
            )
        }
        (rideItems + expenseItems).sortedByDescending { it.timestamp }.take(20)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // === Mock chart data (will be replaced with real aggregation later) ===
    val incomeChartData: List<Float> = listOf(120f, 145f, 98f, 167f, 134f, 185f, 156f)
    val expenseChartData: List<Float> = listOf(35f, 42f, 28f, 55f, 38f, 42f, 30f)
    val incomeSparkline: List<Float> = listOf(0.3f, 0.5f, 0.4f, 0.7f, 0.6f, 0.9f, 0.8f)
    val expenseSparkline: List<Float> = listOf(0.4f, 0.5f, 0.3f, 0.6f, 0.4f, 0.5f, 0.35f)

    // === Actions ===

    fun saveUserName(name: String) {
        viewModelScope.launch {
            shiftManager.saveUserName(name)
        }
    }

    fun startShift(odometerStr: String, costStr: String, timestamp: Long = System.currentTimeMillis()) {
        val odo = odometerStr.replace(",", ".").toDoubleOrNull() ?: return
        val cost = costStr.replace(",", ".").toDoubleOrNull() ?: return
        viewModelScope.launch {
            val shift = Shift(
                startTime = timestamp,
                startOdometer = odo,
                costPerKm = cost
            )
            val shiftId = repository.insertShift(shift).toInt()
            shiftManager.startShift(shiftId, odo, cost, timestamp)
        }
    }

    fun endShift(endOdometerStr: String, expenseCategoryName: String, timestamp: Long = System.currentTimeMillis()) {
        val endOdo = endOdometerStr.replace(",", ".").toDoubleOrNull() ?: return
        val startOdo = startOdometer.value
        val costKm = costPerKm.value
        val currentShiftId = activeShiftId.value

        val distance = endOdo - startOdo
        val rawCost = if (distance > 0) distance * costKm else 0.0
        val totalCost = kotlin.math.round(rawCost * 10) / 10.0

        viewModelScope.launch {
            // Update Shift in DB
            if (currentShiftId != null) {
                val shift = repository.getShiftById(currentShiftId).firstOrNull()
                if (shift != null) {
                    repository.updateShift(shift.copy(
                        endTime = timestamp,
                        endOdometer = endOdo,
                        vehicleCost = totalCost
                    ))
                }
            }

            if (totalCost > 0) {
                repository.insertExpense(totalCost, expenseCategoryName, currentShiftId, timestamp)
            }
            shiftManager.endShift()
        }
    }

    fun addRide(actualStr: String, receiptStr: String, odometerStr: String, timestamp: Long = System.currentTimeMillis()) {
        val actual = actualStr.replace(",", ".").toDoubleOrNull() ?: return
        val receipt = receiptStr.replace(",", ".").toDoubleOrNull() ?: return
        val odo = odometerStr.replace(",", ".").toDoubleOrNull()
        val currentShiftId = activeShiftId.value
        
        viewModelScope.launch {
            repository.insertRide(actual, receipt, currentShiftId, timestamp)
            if (odo != null) {
                shiftManager.updateCurrentOdometer(odo)
            }
        }
    }

    fun addExpense(amountStr: String, category: String, timestamp: Long = System.currentTimeMillis()) {
        val amount = amountStr.replace(",", ".").toDoubleOrNull() ?: return
        val currentShiftId = activeShiftId.value
        
        viewModelScope.launch {
            repository.insertExpense(amount, category, currentShiftId, timestamp)
        }
    }

    // === Shift Detail Queries ===
    fun getAllShifts() = repository.getAllShifts()
    fun getShiftById(shiftId: Int) = repository.getShiftById(shiftId)
    fun getRidesForShift(shiftId: Int) = repository.getRidesForShift(shiftId)
    fun getExpensesForShift(shiftId: Int) = repository.getExpensesForShift(shiftId)

    fun getRecentActivityForShift(shiftId: Int): Flow<List<ActivityItem>> = combine(
        repository.getRidesForShift(shiftId),
        repository.getExpensesForShift(shiftId)
    ) { rideList, expenseList ->
        val rideItems = rideList.map { ride ->
            ActivityItem(
                id = ride.id,
                title = "Κούρσα",
                subtitle = String.format("%.2f € απόδειξη", ride.receiptAmount),
                amount = ride.actualAmount,
                isIncome = true,
                timestamp = ride.timestamp,
                category = "ride"
            )
        }
        val expenseItems = expenseList.map { expense ->
            ActivityItem(
                id = expense.id + 100000,
                title = expense.category,
                subtitle = "",
                amount = expense.amount,
                isIncome = false,
                timestamp = expense.timestamp,
                category = expense.category.lowercase()
            )
        }
        (rideItems + expenseItems).sortedByDescending { it.timestamp }
    }
}
