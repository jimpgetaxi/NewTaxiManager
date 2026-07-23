package com.jimpgetaxi.taximanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val category: String,
    val shiftId: Int? = null,
    val timestamp: Long = System.currentTimeMillis()
)
