package com.openclassrooms.realestatemanager.presentation.common

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.window.layout.WindowMetricsCalculator
import com.openclassrooms.realestatemanager.enums.WindowSizeType

/**
 * Composable function to retrieve and remember the window size class based on the current activity's window metrics.
 *
 * This function uses [rememberWindowSize] to obtain the current window size and calculates the
 * [WindowSizeType] based on the width of the window in density-independent pixels (dp).
 *
 * @return The [WindowSizeType] representing the calculated window size class.
 */
@Composable
fun Activity.rememberWindowSizeClass(): WindowSizeType {

    val windowSize = rememberWindowSize()

    val windowDpSize = with(LocalDensity.current){
        windowSize.toDpSize()
    }

    return getWindowSizeClass(windowSizeDpSize = windowDpSize)
}

/**
 * Composable function to remember the current window size based on the activity's window metrics.
 *
 * This function uses [LocalConfiguration] and [WindowMetricsCalculator] to obtain and remember
 * the size of the current window in density-independent pixels (dp).
 *
 * @return The [Size] representing the width and height of the current window in dp.
 */
@Composable
fun Activity.rememberWindowSize(): Size {
    val configuration = LocalConfiguration.current

    val windowMetric = remember(configuration) {
        WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
    }
    return windowMetric.bounds.toComposeRect().size
}

/**
 * Composable function to determine the window size class based on the provided [windowSizeDpSize].
 *
 * The function categorizes the window size into different classes (Compact, Medium, Expanded) based on
 * the width of the window in density-independent pixels (dp).
 *
 * @param windowSizeDpSize The size of the window in density-independent pixels (dp).
 * @return The [WindowSizeType] representing the calculated window size class.
 * @throws IllegalArgumentException if the width of the window is less than 0.dp.
 */
@Composable
fun getWindowSizeClass(windowSizeDpSize: DpSize) = when{

    windowSizeDpSize.width < 0.dp -> {
        throw IllegalArgumentException("Dp values can not be null!!!!")
    }
    windowSizeDpSize.width < 600.dp -> {
        WindowSizeType.Compact
    }
    windowSizeDpSize.width < 840.dp -> {
        WindowSizeType.Medium
    }
    else -> {
        WindowSizeType.Expanded
    }
}