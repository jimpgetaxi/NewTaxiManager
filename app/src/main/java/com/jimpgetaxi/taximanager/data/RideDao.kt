package com.jimpgetaxi.taximanager.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RideDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRide(ride: Ride)

    @Query("SELECT * FROM rides ORDER BY timestamp DESC")
    fun getAllRides(): Flow<List<Ride>>

    @Query("SELECT SUM(actualAmount) FROM rides")
    fun getTotalRevenue(): Flow<Double?>

    @Query("SELECT SUM(vatAmount) FROM rides")
    fun getTotalVat(): Flow<Double?>
}
