package com.jimpgetaxi.taximanager.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Ride::class, Expense::class, Shift::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rideDao(): RideDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun shiftDao(): ShiftDao
}
