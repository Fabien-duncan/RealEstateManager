package com.openclassrooms.realestatemanager.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.openclassrooms.realestatemanager.enums.ScreenType
import com.openclassrooms.realestatemanager.enums.WindowSizeType
import com.openclassrooms.realestatemanager.presentation.create_property.CreatePropertyViewModel
import com.openclassrooms.realestatemanager.presentation.home.HomeScreen
import com.openclassrooms.realestatemanager.presentation.home.HomeState
import com.openclassrooms.realestatemanager.presentation.home.HomeViewModel

@Composable
fun Navigation(
    windowSize: WindowSizeType,
    homeViewModel: HomeViewModel,
    createPropertyViewModel: CreatePropertyViewModel,
    state: HomeState,
    modifier: Modifier = Modifier
) {
    val isExpanded = windowSize == WindowSizeType.Expanded

    var index by remember{ mutableStateOf(0) }

    var isItemOpened by remember { mutableStateOf(false) }

    val homeScreenType = getScreenType(isExpanded = isExpanded, isDetailOpened = isItemOpened)

    when(homeScreenType){
        ScreenType.List -> {
            HomeScreen(
                state = state,
                onPropertyClicked = {
                    println("element $it has been clicked")
                    isItemOpened = true
                },
                modifier = modifier
            )
        }
        ScreenType.Detail -> {
            println("go to detail")
        }
        ScreenType.ListWithDetail -> {
            println("tablet mode")
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