package com.jimpgetaxi.taximanager.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RideRepository @Inject constructor(
    private val dao: RideDao,
    private val expenseDao: ExpenseDao
) {
    fun getAllRides(): Flow<List<Ride>> = dao.getAllRides()
    fun getTotalRevenue(): Flow<Double?> = dao.getTotalRevenue()
    fun getTotalVat(): Flow<Double?> = dao.getTotalVat()
    
    fun getAllExpenses(): Flow<List<Expense>> = expenseDao.getAllExpenses()
    fun getTotalExpenses(): Flow<Double?> = expenseDao.getTotalExpenses()

    suspend fun insertRide(actualAmount: Double, receiptAmount: Double) {
        // Υπολογισμός ΦΠΑ (13%) βάσει της απόδειξης
        val vatAmount = (receiptAmount / 1.13) * 0.13
        
        val ride = Ride(
            actualAmount = actualAmount,
            receiptAmount = receiptAmount,
            vatAmount = vatAmount
        )
        dao.insertRide(ride)
    }

    suspend fun insertExpense(amount: Double, category: String) {
        val expense = Expense(
            amount = amount,
            category = category
        )
        expenseDao.insertExpense(expense)
    }
}
