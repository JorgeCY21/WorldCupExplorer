package com.example.worldcupexplorer.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = WorldCupRedDark,
    secondary = WorldCupGoldDark,
    tertiary = WorldCupGreenDark,
    background = WorldCupSurfaceDark,
    surface = WorldCupSurfaceDark,
    surfaceVariant = WorldCupBlueDark.copy(alpha = 0.18f),
    outline = WorldCupBlueDark.copy(alpha = 0.35f),
    onPrimary = WorldCupSurfaceDark,
    onSecondary = WorldCupSurfaceDark,
    onTertiary = WorldCupSurfaceDark,
    onBackground = WorldCupSurface,
    onSurface = WorldCupSurface,
    onSurfaceVariant = WorldCupSurface.copy(alpha = 0.72f)
)

private val LightColorScheme = lightColorScheme(
    primary = WorldCupRed,
    secondary = WorldCupGold,
    tertiary = WorldCupGreen,
    background = WorldCupSurface,
    surface = WorldCupSurface,
    surfaceVariant = WorldCupBlue.copy(alpha = 0.08f),
    outline = WorldCupBlue.copy(alpha = 0.22f),
    onPrimary = WorldCupSurface,
    onSecondary = WorldCupBlue,
    onTertiary = WorldCupSurface,
    onBackground = WorldCupBlue,
    onSurface = WorldCupBlue,
    onSurfaceVariant = WorldCupBlue.copy(alpha = 0.72f)
)

@Composable
fun WorldCupExplorerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
