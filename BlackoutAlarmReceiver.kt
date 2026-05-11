package com.corbanswitch.app.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.corbanswitch.app.MainActivity
import com.corbanswitch.app.services.KillSwitchAccessibilityService

class BlackoutAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "com.corbanswitch.ACTION_START_BLACKOUT" -> {
                KillSwitchAccessibilityService.setBlackoutActive(context, true)
                // Launch MainActivity in blackout mode
                val launchIntent = Intent(context, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    putExtra("BLACKOUT_MODE", true)
                }
                context.startActivity(launchIntent)
            }
            "com.corbanswitch.ACTION_END_BLACKOUT" -> {
                KillSwitchAccessibilityService.setBlackoutActive(context, false)
                val launchIntent = Intent(context, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    putExtra("MORNING_RELEASE", true)
                }
                context.startActivity(launchIntent)
            }
        }
    }
}
