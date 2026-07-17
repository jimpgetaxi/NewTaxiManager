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

    @Query("SELECT * FROM rides WHERE timestamp >= :since ORDER BY timestamp DESC")
    fun getRidesSince(since: Long): Flow<List<Ride>>

    @Query("SELECT SUM(actualAmount) FROM rides WHERE timestamp >= :since")
    fun getTotalRevenueSince(since: Long): Flow<Double?>

    @Query("SELECT SUM(vatAmount) FROM rides WHERE timestamp >= :since")
    fun getTotalVatSince(since: Long): Flow<Double?>
}
