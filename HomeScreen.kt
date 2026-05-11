package com.corbanswitch.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.corbanswitch.app.ui.theme.*
import com.corbanswitch.app.viewmodel.KillSwitchViewModel

@Composable
fun HomeScreen(
    viewModel: KillSwitchViewModel,
    onEditSchedule: () -> Unit
) {
    val isArmed by viewModel.isArmed.collectAsState()
    val bedtimeHour by viewModel.bedtimeHour.collectAsState()
    val bedtimeMinute by viewModel.bedtimeMinute.collectAsState()
    val waketimeHour by viewModel.waketimeHour.collectAsState()
    val waketimeMinute by viewModel.waketimeMinute.collectAsState()
    val streak by viewModel.streakCount.collectAsState()
    val last7Days by viewModel.last7Days.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlackBackground)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(DeepRed)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "CORBANSWITCH",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Screen-time kill switch.",
                color = TextSecondary,
                fontSize = 13.sp,
                letterSpacing = 1.sp
            )
        }

        // Toggle
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Switch(
                checked = isArmed,
                onCheckedChange = { viewModel.setArmed(it) },
                modifier = Modifier.size(width = 120.dp, height = 60.dp),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = DeepRed,
                    checkedTrackColor = DimRed.copy(alpha = 0.4f),
                    uncheckedThumbColor = TextSecondary,
                    uncheckedTrackColor = TextDim.copy(alpha = 0.4f)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (isArmed) "ARMED" else "DISARMED",
                color = if (isArmed) DeepRed else TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )
        }

        // Times
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("BEDTIME", color = TextSecondary, fontSize = 11.sp, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        formatTime(bedtimeHour, bedtimeMinute),
                        color = TextPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Light
                    )
                }
                Divider(
                    modifier = Modifier
                        .height(50.dp)
                        .width(1.dp),
                    color = TextDim
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("WAKE TIME", color = TextSecondary, fontSize = 11.sp, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        formatTime(waketimeHour, waketimeMinute),
                        color = TextPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Streak
            if (streak > 0) {
                Text(
                    "🔥 $streak nights respected",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            } else {
                Text("No streak yet.", color = TextDim, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 7 day dots
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                last7Days.forEach { char ->
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (char == '1') GreenDot else RedDot.copy(alpha = 0.4f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onEditSchedule) {
                Text("Edit Schedule", color = TextSecondary, fontSize = 13.sp, letterSpacing = 1.sp)
            }
        }
    }
}

fun formatTime(hour: Int, minute: Int): String {
    val amPm = if (hour < 12) "AM" else "PM"
    val h = if (hour % 12 == 0) 12 else hour % 12
    return "%d:%02d %s".format(h, minute, amPm)
}
