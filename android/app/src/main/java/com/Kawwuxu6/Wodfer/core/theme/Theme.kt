package com.Kawwuxu6.Wodfer.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = darkColorScheme(
    primary = GoldLine,
    onPrimary = OnGold,
    primaryContainer = VelvetGlow,
    onPrimaryContainer = Ivory,
    secondary = RubyMark,
    onSecondary = Ivory,
    secondaryContainer = RubyDeep,
    onSecondaryContainer = Ivory,
    tertiary = GoldLight,
    onTertiary = OnGold,
    background = InkBlack,
    onBackground = Ivory,
    surface = VelvetCard,
    onSurface = Ivory,
    surfaceVariant = VelvetDeep,
    onSurfaceVariant = IvoryDim,
    error = RubyBright,
    onError = Ivory,
    outline = GoldDim,
    outlineVariant = Ash,
    scrim = InkDeep
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}
