package com.corbanswitch.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.corbanswitch.app.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ResistanceLockScreen(
    onUnlocked: () -> Unit,
    onGaveUp: () -> Unit
) {
    var secondsLeft by remember { mutableStateOf(60) }
    var confirmationStep by remember { mutableStateOf(0) }
    var timerFinished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000L)
            secondsLeft--
        }
        timerFinished = true
    }

    val messages = listOf(
        "Are you sure? Your streak depends on this.",
        "Last warning. You set this for a reason.",
        "Final chance. Go to sleep."
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlackBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(40.dp)
        ) {

            // Countdown
            Text(
                text = if (!timerFinished) "$secondsLeft" else "—",
                color = TextDim,
                fontSize = 80.sp,
                fontWeight = FontWeight.Thin
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Message
            Text(
                text = messages.getOrElse(confirmationStep) { messages.last() },
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Override button — only active after timer finishes
            Button(
                onClick = {
                    if (timerFinished) {
                        if (confirmationStep < 2) {
                            confirmationStep++
                        } else {
                            onUnlocked()
                        }
                    }
                },
                enabled = timerFinished,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DeepRed,
                    disabledContainerColor = DimRed.copy(alpha = 0.2f)
                )
            ) {
                Text(
                    "I want to override",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(onClick = onGaveUp) {
                Text(
                    "Never mind",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        }
    }
}
