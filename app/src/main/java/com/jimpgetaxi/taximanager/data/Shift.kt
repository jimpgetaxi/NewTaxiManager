package com.jimpgetaxi.taximanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shifts")
data class Shift(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val startTime: Long,
    val endTime: Long? = null,
    val startOdometer: Double,
    val endOdometer: Double? = null,
    val costPerKm: Double,
    val vehicleCost: Double? = null
)
