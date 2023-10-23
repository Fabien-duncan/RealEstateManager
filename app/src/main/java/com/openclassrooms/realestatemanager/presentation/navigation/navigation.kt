package com.openclassrooms.realestatemanager.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.openclassrooms.realestatemanager.enums.ScreenType
import com.openclassrooms.realestatemanager.enums.WindowSize

@Composable
fun navigation(
    windowSize: WindowSize
) {
    val isExpanded = windowSize == WindowSize.Expanded

    var index by remember{ mutableStateOf(0) }

    var isItemOpened by remember { mutableStateOf(false) }

    val homeScreenType = getScreenType(isExpanded = isExpanded, isDetailOpened = isItemOpened)

    when(homeScreenType){
        ScreenType.List -> {

        }
        ScreenType.Detail -> {

        }
        ScreenType.ListWithDetail -> {

        }
    }

}

@Composable
fun getScreenType(
    isExpanded:Boolean,
    isDetailOpened:Boolean
): ScreenType = when(isExpanded){
    false -> {
        if(isDetailOpened){
            ScreenType.Detail
        }else{
            ScreenType.List
        }
    }
    true -> ScreenType.ListWithDetail
}