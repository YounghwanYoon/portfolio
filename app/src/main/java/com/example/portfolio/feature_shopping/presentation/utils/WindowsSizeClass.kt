package com.example.portfolio.feature_shopping.presentation.utils

import android.app.Activity
import androidx.compose.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.layout.WindowMetricsCalculator

data class WindowSizeClass(
    val widthWindowSizeClass: WindowType,
    val heightWindowSizeClass: WindowType,
    val widthWindowDpSize: Dp,
    val heightWindowDpSize: Dp
) {
    sealed class WindowType {
        //when device in vertical mode
        object COMPACT : WindowType()
        //when device in rotated to horizontal mode
        object MEDIUM : WindowType()
        //when device is tablet
        object EXPANDED : WindowType()
    }
}


@androidx.compose.runtime.Composable
fun rememberWindowSizeClass(activity: Activity): WindowSizeClass {
    val configuration = LocalConfiguration.current
    val windowMetrics = remember(configuration) {
        WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(activity = activity)
    }

    val windowDpSize = with(LocalDensity.current) {
        windowMetrics.bounds.toComposeRect().size.toDpSize()
    }

    return WindowSizeClass(
        widthWindowSizeClass = when {
            windowDpSize.width < 0.dp -> throw IllegalArgumentException("Dp value cannot be negative")
            windowDpSize.width < 600.dp -> WindowSizeClass.WindowType.COMPACT
            windowDpSize.width < 840.dp -> WindowSizeClass.WindowType.MEDIUM
            else -> WindowSizeClass.WindowType.EXPANDED
        },
        heightWindowSizeClass = when {
            windowDpSize.height < 0.dp -> throw IllegalArgumentException("Dp value cannot be negative")
            windowDpSize.height < 480.dp -> WindowSizeClass.WindowType.COMPACT
            windowDpSize.height < 900.dp -> WindowSizeClass.WindowType.MEDIUM
            else -> WindowSizeClass.WindowType.EXPANDED
        },
        widthWindowDpSize = windowDpSize.width,
        heightWindowDpSize = windowDpSize.height
    )
}