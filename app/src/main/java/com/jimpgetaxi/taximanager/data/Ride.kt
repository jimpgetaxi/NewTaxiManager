package com.jimpgetaxi.taximanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rides")
data class Ride(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val actualAmount: Double,
    val receiptAmount: Double,
    val vatAmount: Double,
    val shiftId: Int? = null,
    val timestamp: Long = System.currentTimeMillis()
)
