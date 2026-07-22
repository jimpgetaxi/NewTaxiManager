package com.jimpgetaxi.taximanager.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "shift_prefs")

@Singleton
class ShiftManager @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        val IS_SHIFT_ACTIVE = booleanPreferencesKey("is_shift_active")
        val START_ODOMETER = doublePreferencesKey("start_odometer")
        val COST_PER_KM = doublePreferencesKey("cost_per_km")
        val SHIFT_START_TIME = longPreferencesKey("shift_start_time")
        val CURRENT_ODOMETER = doublePreferencesKey("current_odometer")
        val USER_NAME = stringPreferencesKey("user_name")
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
    }

    val isShiftActiveFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[IS_SHIFT_ACTIVE] ?: false
    }

    val startOdometerFlow: Flow<Double> = context.dataStore.data.map { prefs ->
        prefs[START_ODOMETER] ?: 0.0
    }

    val costPerKmFlow: Flow<Double> = context.dataStore.data.map { prefs ->
        prefs[COST_PER_KM] ?: 0.22 // Default value
    }
    
    val shiftStartTimeFlow: Flow<Long> = context.dataStore.data.map { prefs ->
        prefs[SHIFT_START_TIME] ?: 0L
    }
    
    val currentOdometerFlow: Flow<Double> = context.dataStore.data.map { prefs ->
        prefs[CURRENT_ODOMETER] ?: 0.0
    }

    val userNameFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[USER_NAME] ?: ""
    }

    val onboardingDoneFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[ONBOARDING_DONE] ?: false
    }

    suspend fun startShift(startOdometer: Double, costPerKm: Double, timestamp: Long = System.currentTimeMillis()) {
        context.dataStore.edit { prefs ->
            prefs[IS_SHIFT_ACTIVE] = true
            prefs[START_ODOMETER] = startOdometer
            prefs[COST_PER_KM] = costPerKm
            prefs[SHIFT_START_TIME] = timestamp
            prefs[CURRENT_ODOMETER] = startOdometer
        }
    }

    suspend fun updateCurrentOdometer(odometer: Double) {
        context.dataStore.edit { prefs ->
            prefs[CURRENT_ODOMETER] = odometer
        }
    }

    suspend fun endShift() {
        context.dataStore.edit { prefs ->
            prefs[IS_SHIFT_ACTIVE] = false
            // We intentionally do not clear COST_PER_KM so the app remembers the last used value!
        }
    }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_NAME] = name
            prefs[ONBOARDING_DONE] = true
        }
    }
}
