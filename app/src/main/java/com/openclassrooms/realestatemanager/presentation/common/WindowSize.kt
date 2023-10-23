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
import com.openclassrooms.realestatemanager.enums.WindowSize

@Composable
fun Activity.rememberWindowSizeClass(): WindowSize {

    val windowSize = rememberWindowSize()

    val windowDpSize = with(LocalDensity.current){
        windowSize.toDpSize()
    }

    return getWindowSizeClass(windowSizeDpSize = windowDpSize)
}

@Composable
fun Activity.rememberWindowSize(): Size {
    val configuration = LocalConfiguration.current

    val windowMetric = remember(configuration) {
        WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
    }
    return windowMetric.bounds.toComposeRect().size
}

@Composable
fun getWindowSizeClass(windowSizeDpSize: DpSize) = when{

    windowSizeDpSize.width < 0.dp -> {
        throw IllegalArgumentException("Dp values can not be null!!!!")
    }
    windowSizeDpSize.width < 600.dp -> {
        WindowSize.Compact
    }
    windowSizeDpSize.width < 840.dp -> {
        WindowSize.Medium
    }
    else -> {
        WindowSize.Expanded
    }
}