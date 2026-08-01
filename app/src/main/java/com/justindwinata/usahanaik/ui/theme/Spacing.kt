package com.justindwinata.usahanaik.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class UsahaNaikSpacing(
    val xxs: Dp = 2.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 40.dp,
    val xxl: Dp = 32.dp,
    val xxxl: Dp = 48.dp,
    val gutter: Dp = 16.dp,
    val containerMargin: Dp = 20.dp,
    val base: Dp = 8.dp
)

@Immutable
data class UsahaNaikElevation(
    val none: Dp = 0.dp,
    val xs: Dp = 1.dp,
    val sm: Dp = 2.dp,
    val md: Dp = 4.dp,
    val lg: Dp = 8.dp,
    val xl: Dp = 12.dp,
    val xxl: Dp = 16.dp
)

@Immutable
data class UsahaNaikRadius(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 20.dp,
    val full: Dp = 999.dp
)

val AppSpacing = UsahaNaikSpacing()
val AppElevation = UsahaNaikElevation()
val AppRadius = UsahaNaikRadius()
