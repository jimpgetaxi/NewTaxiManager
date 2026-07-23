package com.jimpgetaxi.taximanager.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RideRepository @Inject constructor(
    private val dao: RideDao,
    private val expenseDao: ExpenseDao,
    private val shiftDao: ShiftDao
) {
    fun getAllRides(): Flow<List<Ride>> = dao.getAllRides()
    fun getTotalRevenue(): Flow<Double?> = dao.getTotalRevenue()
    fun getTotalVat(): Flow<Double?> = dao.getTotalVat()
    
    fun getAllExpenses(): Flow<List<Expense>> = expenseDao.getAllExpenses()
    fun getTotalExpenses(): Flow<Double?> = expenseDao.getTotalExpenses()

    fun getRidesSince(since: Long): Flow<List<Ride>> = dao.getRidesSince(since)
    fun getTotalRevenueSince(since: Long): Flow<Double?> = dao.getTotalRevenueSince(since)
    fun getTotalVatSince(since: Long): Flow<Double?> = dao.getTotalVatSince(since)
    fun getTotalExpensesSince(since: Long): Flow<Double?> = expenseDao.getTotalExpensesSince(since)
    fun getExpensesSince(since: Long): Flow<List<Expense>> = expenseDao.getExpensesSince(since)

    fun getRidesForShift(shiftId: Int): Flow<List<Ride>> = dao.getRidesForShift(shiftId)
    fun getExpensesForShift(shiftId: Int): Flow<List<Expense>> = expenseDao.getExpensesForShift(shiftId)

    suspend fun insertRide(actualAmount: Double, receiptAmount: Double, shiftId: Int?, timestamp: Long = System.currentTimeMillis()) {
        // Υπολογισμός ΦΠΑ (13%) βάσει της απόδειξης
        val vatAmount = (receiptAmount / 1.13) * 0.13
        
        val ride = Ride(
            actualAmount = actualAmount,
            receiptAmount = receiptAmount,
            vatAmount = vatAmount,
            shiftId = shiftId,
            timestamp = timestamp
        )
        dao.insertRide(ride)
    }

    suspend fun insertExpense(amount: Double, category: String, shiftId: Int?, timestamp: Long = System.currentTimeMillis()) {
        val expense = Expense(
            amount = amount,
            category = category,
            shiftId = shiftId,
            timestamp = timestamp
        )
        expenseDao.insertExpense(expense)
    }

    // Shift Operations
    fun getAllShifts(): Flow<List<Shift>> = shiftDao.getAllShifts()
    
    fun getShiftById(shiftId: Int): Flow<Shift> = shiftDao.getShiftById(shiftId)

    suspend fun insertShift(shift: Shift): Long = shiftDao.insertShift(shift)

    suspend fun updateShift(shift: Shift) = shiftDao.updateShift(shift)

    suspend fun deleteShift(shift: Shift) = shiftDao.deleteShift(shift)
}
