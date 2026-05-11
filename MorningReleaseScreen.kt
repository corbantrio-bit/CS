package com.corbanswitch.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
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
fun MorningReleaseScreen(onDone: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000L)
        onDone()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlackBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Good morning.",
                color = TextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "You earned it.",
                color = TextSecondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}
