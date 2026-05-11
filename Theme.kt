package com.corbanswitch.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BlackBackground = Color(0xFF000000)
val DarkSurface = Color(0xFF0A0A0A)
val CardBackground = Color(0xFF111111)
val DeepRed = Color(0xFFCC0000)
val DimRed = Color(0xFF880000)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFF888888)
val TextDim = Color(0xFF444444)
val GreenDot = Color(0xFF2ECC71)
val RedDot = Color(0xFFCC0000)

private val CorbanDarkColorScheme = darkColorScheme(
    primary = DeepRed,
    onPrimary = TextPrimary,
    background = BlackBackground,
    surface = DarkSurface,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    secondary = TextSecondary,
    error = DeepRed
)

@Composable
fun CorbanSwitchTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CorbanDarkColorScheme,
        content = content
    )
}
