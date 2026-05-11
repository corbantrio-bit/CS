package com.corbanswitch.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.corbanswitch.app.ui.theme.*
import com.corbanswitch.app.viewmodel.KillSwitchViewModel

@Composable
fun TimeSetupScreen(
    viewModel: KillSwitchViewModel,
    onSave: () -> Unit
) {
    val bedtimeHour by viewModel.bedtimeHour.collectAsState()
    val bedtimeMinute by viewModel.bedtimeMinute.collectAsState()
    val waketimeHour by viewModel.waketimeHour.collectAsState()
    val waketimeMinute by viewModel.waketimeMinute.collectAsState()

    var localBedHour by remember { mutableStateOf(bedtimeHour) }
    var localBedMin by remember { mutableStateOf(bedtimeMinute) }
    var localWakeHour by remember { mutableStateOf(waketimeHour) }
    var localWakeMin by remember { mutableStateOf(waketimeMinute) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlackBackground)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            "EDIT SCHEDULE",
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 4.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Bedtime
        Text("BEDTIME", color = TextSecondary, fontSize = 11.sp, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(12.dp))
        TimePickerRow(
            hour = localBedHour,
            minute = localBedMin,
            onHourChange = { localBedHour = it },
            onMinuteChange = { localBedMin = it }
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Wake time
        Text("WAKE TIME", color = TextSecondary, fontSize = 11.sp, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(12.dp))
        TimePickerRow(
            hour = localWakeHour,
            minute = localWakeMin,
            onHourChange = { localWakeHour = it },
            onMinuteChange = { localWakeMin = it }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.saveBedtime(localBedHour, localBedMin)
                viewModel.saveWaketime(localWakeHour, localWakeMin)
                onSave()
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DeepRed)
        ) {
            Text("SAVE", color = TextPrimary, fontSize = 15.sp, letterSpacing = 3.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun TimePickerRow(
    hour: Int,
    minute: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        NumberPicker(
            value = hour,
            range = 0..23,
            onValueChange = onHourChange,
            format = { "%02d".format(it) }
        )
        Text(" : ", color = TextPrimary, fontSize = 28.sp)
        NumberPicker(
            value = minute,
            range = 0..59,
            onValueChange = onMinuteChange,
            format = { "%02d".format(it) }
        )
    }
}

@Composable
fun NumberPicker(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    format: (Int) -> String = { it.toString() }
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TextButton(onClick = {
            val next = if (value >= range.last) range.first else value + 1
            onValueChange(next)
        }) {
            Text("▲", color = TextSecondary, fontSize = 18.sp)
        }
        Text(
            text = format(value),
            color = TextPrimary,
            fontSize = 36.sp,
            fontWeight = FontWeight.Light
        )
        TextButton(onClick = {
            val prev = if (value <= range.first) range.last else value - 1
            onValueChange(prev)
        }) {
            Text("▼", color = TextSecondary, fontSize = 18.sp)
        }
    }
}
