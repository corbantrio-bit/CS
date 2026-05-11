package com.corbanswitch.app

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.corbanswitch.app.receivers.KillSwitchDeviceAdminReceiver
import com.corbanswitch.app.services.KillSwitchAccessibilityService
import com.corbanswitch.app.services.KillSwitchService
import com.corbanswitch.app.ui.screens.*
import com.corbanswitch.app.ui.theme.CorbanSwitchTheme
import com.corbanswitch.app.viewmodel.KillSwitchViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: KillSwitchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep screen on and show over lock screen during blackout
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        val blackoutMode = intent.getBooleanExtra("BLACKOUT_MODE", false)
        val morningRelease = intent.getBooleanExtra("MORNING_RELEASE", false)

        setContent {
            CorbanSwitchTheme {
                CorbanSwitchApp(
                    viewModel = viewModel,
                    startInBlackout = blackoutMode,
                    startInMorning = morningRelease
                )
            }
        }

        // Start foreground service
        KillSwitchService.start(this)

        // Request permissions on first run
        requestPermissionsIfNeeded()
    }

    private fun requestPermissionsIfNeeded() {
        // Request Device Admin
        val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val adminComponent = ComponentName(this, KillSwitchDeviceAdminReceiver::class.java)
        if (!dpm.isAdminActive(adminComponent)) {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent)
                putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "CorbanSwitch needs Device Admin access to enforce your bedtime lockout.")
            }
            startActivity(intent)
        }

        // Request Accessibility Service
        if (!isAccessibilityServiceEnabled()) {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val service = "$packageName/${KillSwitchAccessibilityService::class.java.canonicalName}"
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        return enabledServices.contains(service)
    }
}

@Composable
fun CorbanSwitchApp(
    viewModel: KillSwitchViewModel,
    startInBlackout: Boolean,
    startInMorning: Boolean
) {
    val isBlackoutActive by viewModel.isBlackoutActive.collectAsState()

    var screen by remember {
        mutableStateOf(
            when {
                startInMorning -> "morning"
                startInBlackout || isBlackoutActive -> "blackout"
                else -> "home"
            }
        )
    }
    var showResistance by remember { mutableStateOf(false) }

    LaunchedEffect(isBlackoutActive) {
        if (isBlackoutActive && screen != "blackout" && screen != "resistance") {
            screen = "blackout"
        }
    }

    when {
        screen == "morning" -> MorningReleaseScreen(onDone = {
            viewModel.endBlackout(overridden = false)
            screen = "home"
        })
        screen == "blackout" && !showResistance -> BlackoutScreen(onTap = {
            showResistance = true
        })
        screen == "blackout" && showResistance -> ResistanceLockScreen(
            onUnlocked = {
                viewModel.endBlackout(overridden = true)
                KillSwitchAccessibilityService.setBlackoutActive(
                    androidx.compose.ui.platform.LocalContext.current,
                    false
                )
                screen = "home"
                showResistance = false
            },
            onGaveUp = { showResistance = false }
        )
        screen == "home" -> HomeScreen(
            viewModel = viewModel,
            onEditSchedule = { screen = "timesetup" }
        )
        screen == "timesetup" -> TimeSetupScreen(
            viewModel = viewModel,
            onSave = { screen = "home" }
        )
    }
}
