package com.jimpgetaxi.taximanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShiftDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShift(shift: Shift): Long

    @Update
    suspend fun updateShift(shift: Shift)

    @Delete
    suspend fun deleteShift(shift: Shift)

    @Query("SELECT * FROM shifts ORDER BY startTime DESC")
    fun getAllShifts(): Flow<List<Shift>>

    @Query("SELECT * FROM shifts WHERE id = :shiftId")
    fun getShiftById(shiftId: Int): Flow<Shift>
}
