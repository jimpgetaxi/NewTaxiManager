package com.jimpgetaxi.taximanager.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses")
    fun getTotalExpenses(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM expenses WHERE timestamp >= :since")
    fun getTotalExpensesSince(since: Long): Flow<Double?>

    @Query("SELECT * FROM expenses WHERE shiftId = :shiftId ORDER BY timestamp DESC")
    fun getExpensesForShift(shiftId: Int): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE timestamp >= :since ORDER BY timestamp DESC")
    fun getExpensesSince(since: Long): Flow<List<Expense>>
}
