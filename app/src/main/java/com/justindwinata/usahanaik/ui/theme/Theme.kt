package com.justindwinata.usahanaik.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = CoralPrimary,
    onPrimary = SurfaceWarm,
    primaryContainer = CoralSoft,
    onPrimaryContainer = CoralDeep,
    secondary = GreenPositive,
    onSecondary = SurfaceWarm,
    secondaryContainer = GreenSoft,
    onSecondaryContainer = GreenDeep,
    tertiary = BlueDeep,
    tertiaryContainer = BlueSoft,
    background = CanvasSoft,
    onBackground = Ink,
    surface = SurfaceWarm,
    onSurface = Ink,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = InkMuted,
    outline = BorderSubtle,
    outlineVariant = BorderStrong,
    error = RoseDeep,
    errorContainer = RoseSoft
)

@Composable
fun UsahaNaikTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = CanvasSoft.toArgb()
            window.navigationBarColor = SurfaceWarm.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = UsahaNaikTypography,
        content = content
    )
}
