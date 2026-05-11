package com.corbanswitch.app.viewmodel

import android.app.Application
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.corbanswitch.app.data.ScheduleRepository
import com.corbanswitch.app.receivers.BlackoutAlarmReceiver
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class KillSwitchViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ScheduleRepository(application)

    val bedtimeHour: StateFlow<Int> = repo.bedtimeHour.stateIn(viewModelScope, SharingStarted.Eagerly, 22)
    val bedtimeMinute: StateFlow<Int> = repo.bedtimeMinute.stateIn(viewModelScope, SharingStarted.Eagerly, 0)
    val waketimeHour: StateFlow<Int> = repo.waketimeHour.stateIn(viewModelScope, SharingStarted.Eagerly, 7)
    val waketimeMinute: StateFlow<Int> = repo.waketimeMinute.stateIn(viewModelScope, SharingStarted.Eagerly, 0)
    val isArmed: StateFlow<Boolean> = repo.isArmed.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val isBlackoutActive: StateFlow<Boolean> = repo.isBlackoutActive.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val streakCount: StateFlow<Int> = repo.streakCount.stateIn(viewModelScope, SharingStarted.Eagerly, 0)
    val last7Days: StateFlow<String> = repo.last7Days.stateIn(viewModelScope, SharingStarted.Eagerly, "0000000")

    fun setArmed(armed: Boolean) {
        viewModelScope.launch {
            repo.setArmed(armed)
            if (armed) scheduleAlarms() else cancelAlarms()
        }
    }

    fun saveBedtime(hour: Int, minute: Int) {
        viewModelScope.launch { repo.saveBedtime(hour, minute) }
    }

    fun saveWaketime(hour: Int, minute: Int) {
        viewModelScope.launch { repo.saveWaketime(hour, minute) }
    }

    fun startBlackout() {
        viewModelScope.launch { repo.setBlackoutActive(true) }
    }

    fun endBlackout(overridden: Boolean) {
        viewModelScope.launch {
            repo.setBlackoutActive(false)
            if (overridden) repo.resetStreak() else repo.incrementStreak()
        }
    }

    private fun scheduleAlarms() {
        val context = getApplication<Application>()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Schedule bedtime alarm
        val bedtimeCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, bedtimeHour.value)
            set(Calendar.MINUTE, bedtimeMinute.value)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) add(Calendar.DAY_OF_MONTH, 1)
        }

        val startIntent = Intent(context, BlackoutAlarmReceiver::class.java).apply {
            action = "com.corbanswitch.ACTION_START_BLACKOUT"
        }
        val startPending = PendingIntent.getBroadcast(
            context, 1001, startIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, bedtimeCal.timeInMillis, startPending)

        // Schedule wake time alarm
        val waketimeCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, waketimeHour.value)
            set(Calendar.MINUTE, waketimeMinute.value)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) add(Calendar.DAY_OF_MONTH, 1)
        }

        val endIntent = Intent(context, BlackoutAlarmReceiver::class.java).apply {
            action = "com.corbanswitch.ACTION_END_BLACKOUT"
        }
        val endPending = PendingIntent.getBroadcast(
            context, 1002, endIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, waketimeCal.timeInMillis, endPending)
    }

    private fun cancelAlarms() {
        val context = getApplication<Application>()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val startIntent = Intent(context, BlackoutAlarmReceiver::class.java)
        val startPending = PendingIntent.getBroadcast(
            context, 1001, startIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(startPending)

        val endIntent = Intent(context, BlackoutAlarmReceiver::class.java)
        val endPending = PendingIntent.getBroadcast(
            context, 1002, endIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(endPending)
    }
}
