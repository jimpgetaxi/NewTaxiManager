package com.jimpgetaxi.taximanager.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Ride::class, Expense::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val rideDao: RideDao
    abstract val expenseDao: ExpenseDao
}
