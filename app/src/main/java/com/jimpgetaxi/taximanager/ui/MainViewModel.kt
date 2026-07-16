package com.jimpgetaxi.taximanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimpgetaxi.taximanager.data.Ride
import com.jimpgetaxi.taximanager.data.RideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RideRepository
) : ViewModel() {

    val rides: StateFlow<List<Ride>> = repository.getAllRides()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalRevenue: StateFlow<Double> = repository.getTotalRevenue()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalVat: StateFlow<Double> = repository.getTotalVat()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalExpenses: StateFlow<Double> = repository.getTotalExpenses()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val netProfit: StateFlow<Double> = combine(totalRevenue, totalVat, totalExpenses) { rev, vat, exp ->
        rev - vat - exp
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun addRide(actualStr: String, receiptStr: String) {
        val actual = actualStr.replace(",", ".").toDoubleOrNull() ?: return
        val receipt = receiptStr.replace(",", ".").toDoubleOrNull() ?: return
        
        viewModelScope.launch {
            repository.insertRide(actual, receipt)
        }
    }

    fun addExpense(amountStr: String, category: String) {
        val amount = amountStr.replace(",", ".").toDoubleOrNull() ?: return
        
        viewModelScope.launch {
            repository.insertExpense(amount, category)
        }
    }
}
