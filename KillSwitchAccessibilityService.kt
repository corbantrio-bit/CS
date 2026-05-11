package com.corbanswitch.app.services

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.SharedPreferences
import android.view.accessibility.AccessibilityEvent
import android.view.KeyEvent

class KillSwitchAccessibilityService : AccessibilityService() {

    private lateinit var prefs: SharedPreferences

    override fun onServiceConnected() {
        super.onServiceConnected()
        prefs = getSharedPreferences("corbanswitch_service", Context.MODE_PRIVATE)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (!isBlackoutActive()) return
        // Block any attempt to leave the blackout screen
        val packageName = event?.packageName?.toString() ?: return
        if (packageName != "com.corbanswitch.app") {
            performGlobalAction(GLOBAL_ACTION_BACK)
        }
    }

    override fun onKeyEvent(event: KeyEvent?): Boolean {
        if (!isBlackoutActive()) return super.onKeyEvent(event)
        // Block home, back, recents during blackout
        return when (event?.keyCode) {
            KeyEvent.KEYCODE_HOME,
            KeyEvent.KEYCODE_APP_SWITCH,
            KeyEvent.KEYCODE_BACK -> true // consumed, blocked
            else -> super.onKeyEvent(event)
        }
    }

    override fun onInterrupt() {}

    private fun isBlackoutActive(): Boolean {
        return prefs.getBoolean("blackout_active", false)
    }

    companion object {
        fun setBlackoutActive(context: Context, active: Boolean) {
            context.getSharedPreferences("corbanswitch_service", Context.MODE_PRIVATE)
                .edit().putBoolean("blackout_active", active).apply()
        }
    }
}
