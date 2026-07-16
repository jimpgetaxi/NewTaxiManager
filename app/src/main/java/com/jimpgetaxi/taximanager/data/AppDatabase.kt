package com.jimpgetaxi.taximanager.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Ride::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val rideDao: RideDao
}
