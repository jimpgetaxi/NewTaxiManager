package com.jimpgetaxi.taximanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimpgetaxi.taximanager.data.Ride
import com.jimpgetaxi.taximanager.data.RideRepository
import com.jimpgetaxi.taximanager.data.ShiftManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

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

    val startOdometer: StateFlow<Double> = shiftManager.startOdometerFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val costPerKm: StateFlow<Double> = shiftManager.costPerKmFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.22)

    val currentOdometer: StateFlow<Double> = shiftManager.currentOdometerFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

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

    fun startShift(odometerStr: String, costStr: String) {
        val odo = odometerStr.replace(",", ".").toDoubleOrNull() ?: return
        val cost = costStr.replace(",", ".").toDoubleOrNull() ?: return
        viewModelScope.launch {
            shiftManager.startShift(odo, cost)
        }
    }

    fun endShift(endOdometerStr: String, expenseCategoryName: String) {
        val endOdo = endOdometerStr.replace(",", ".").toDoubleOrNull() ?: return
        val startOdo = startOdometer.value
        val costKm = costPerKm.value

        val distance = endOdo - startOdo
        if (distance > 0) {
            val rawCost = distance * costKm
            val totalCost = kotlin.math.round(rawCost * 10) / 10.0
            viewModelScope.launch {
                repository.insertExpense(totalCost, expenseCategoryName)
                shiftManager.endShift()
            }
        } else {
            // Αν είναι <= 0, απλά κλείνουμε τη βάρδια
            viewModelScope.launch {
                shiftManager.endShift()
            }
        }
    }

    fun addRide(actualStr: String, receiptStr: String, odometerStr: String) {
        val actual = actualStr.replace(",", ".").toDoubleOrNull() ?: return
        val receipt = receiptStr.replace(",", ".").toDoubleOrNull() ?: return
        val odo = odometerStr.replace(",", ".").toDoubleOrNull()
        
        viewModelScope.launch {
            repository.insertRide(actual, receipt)
            if (odo != null) {
                shiftManager.updateCurrentOdometer(odo)
            }
        }
    }

    fun addExpense(amountStr: String, category: String) {
        val amount = amountStr.replace(",", ".").toDoubleOrNull() ?: return
        
        viewModelScope.launch {
            repository.insertExpense(amount, category)
        }
    }
}
