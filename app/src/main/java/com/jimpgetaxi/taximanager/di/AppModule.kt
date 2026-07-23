package com.jimpgetaxi.taximanager.di

import android.app.Application
import androidx.room.Room
import com.jimpgetaxi.taximanager.data.AppDatabase
import com.jimpgetaxi.taximanager.data.RideDao
import com.jimpgetaxi.taximanager.data.ExpenseDao
import com.jimpgetaxi.taximanager.data.ShiftDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "taxi_manager_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideRideDao(db: AppDatabase): RideDao {
        return db.rideDao()
    }

    @Provides
    @Singleton
    fun provideExpenseDao(db: AppDatabase): ExpenseDao {
        return db.expenseDao()
    }

    @Provides
    @Singleton
    fun provideShiftDao(db: AppDatabase): ShiftDao {
        return db.shiftDao()
    }
}
