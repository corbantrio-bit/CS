package com.corbanswitch.app.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.corbanswitch.app.services.KillSwitchService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON") {
            // Restart foreground service after boot
            KillSwitchService.start(context)
        }
    }
}
