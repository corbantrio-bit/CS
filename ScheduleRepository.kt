package com.corbanswitch.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "corbanswitch_prefs")

object PreferencesKeys {
    val BEDTIME_HOUR = intPreferencesKey("bedtime_hour")
    val BEDTIME_MINUTE = intPreferencesKey("bedtime_minute")
    val WAKETIME_HOUR = intPreferencesKey("waketime_hour")
    val WAKETIME_MINUTE = intPreferencesKey("waketime_minute")
    val IS_ARMED = booleanPreferencesKey("is_armed")
    val IS_BLACKOUT_ACTIVE = booleanPreferencesKey("is_blackout_active")
    val STREAK_COUNT = intPreferencesKey("streak_count")
    val LAST_7_DAYS = stringPreferencesKey("last_7_days")
}

class ScheduleRepository(private val context: Context) {

    val bedtimeHour: Flow<Int> = context.dataStore.data.map { it[PreferencesKeys.BEDTIME_HOUR] ?: 22 }
    val bedtimeMinute: Flow<Int> = context.dataStore.data.map { it[PreferencesKeys.BEDTIME_MINUTE] ?: 0 }
    val waketimeHour: Flow<Int> = context.dataStore.data.map { it[PreferencesKeys.WAKETIME_HOUR] ?: 7 }
    val waketimeMinute: Flow<Int> = context.dataStore.data.map { it[PreferencesKeys.WAKETIME_MINUTE] ?: 0 }
    val isArmed: Flow<Boolean> = context.dataStore.data.map { it[PreferencesKeys.IS_ARMED] ?: false }
    val isBlackoutActive: Flow<Boolean> = context.dataStore.data.map { it[PreferencesKeys.IS_BLACKOUT_ACTIVE] ?: false }
    val streakCount: Flow<Int> = context.dataStore.data.map { it[PreferencesKeys.STREAK_COUNT] ?: 0 }
    val last7Days: Flow<String> = context.dataStore.data.map { it[PreferencesKeys.LAST_7_DAYS] ?: "0000000" }

    suspend fun saveBedtime(hour: Int, minute: Int) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.BEDTIME_HOUR] = hour
            prefs[PreferencesKeys.BEDTIME_MINUTE] = minute
        }
    }

    suspend fun saveWaketime(hour: Int, minute: Int) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.WAKETIME_HOUR] = hour
            prefs[PreferencesKeys.WAKETIME_MINUTE] = minute
        }
    }

    suspend fun setArmed(armed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.IS_ARMED] = armed
        }
    }

    suspend fun setBlackoutActive(active: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.IS_BLACKOUT_ACTIVE] = active
        }
    }

    suspend fun incrementStreak() {
        context.dataStore.edit { prefs ->
            val current = prefs[PreferencesKeys.STREAK_COUNT] ?: 0
            prefs[PreferencesKeys.STREAK_COUNT] = current + 1
            val days = prefs[PreferencesKeys.LAST_7_DAYS] ?: "0000000"
            prefs[PreferencesKeys.LAST_7_DAYS] = (days.drop(1) + "1").takeLast(7)
        }
    }

    suspend fun resetStreak() {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.STREAK_COUNT] = 0
            val days = prefs[PreferencesKeys.LAST_7_DAYS] ?: "0000000"
            prefs[PreferencesKeys.LAST_7_DAYS] = (days.drop(1) + "0").takeLast(7)
        }
    }
}
