package com.jimpgetaxi.taximanager.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RideRepository @Inject constructor(
    private val dao: RideDao
) {
    fun getAllRides(): Flow<List<Ride>> = dao.getAllRides()
    fun getTotalRevenue(): Flow<Double?> = dao.getTotalRevenue()
    fun getTotalVat(): Flow<Double?> = dao.getTotalVat()

    suspend fun insertRide(actualAmount: Double, receiptAmount: Double) {
        // Υπολογισμός ΦΠΑ (13%) βάσει της απόδειξης
        val vatAmount = (receiptAmount / 1.13) * 0.13
        
        val ride = Ride(
            actualAmount = actualAmount,
            receiptAmount = receiptAmount,
            vatAmount = vatAmount
        )
        dao.insertRide(ride)
    }
}
