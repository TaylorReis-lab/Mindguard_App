package com.mindguard.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MindguardColorScheme = darkColorScheme(
    primary = PurplePrimary,
    secondary = SuccessGreen,
    tertiary = WarningAmber,
    background = BgDeep,
    surface = Color(0xFF1E293B),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    error = DangerRed,
    outline = GlassBorder
)

@Composable
fun MindguardappTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MindguardColorScheme,
        typography = Typography,
        content = content
    )
}
