package com.openclassrooms.realestatemanager.presentation.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.common.ScreenViewState
import com.openclassrooms.realestatemanager.enums.ScreenType
import com.openclassrooms.realestatemanager.enums.WindowSizeType
import com.openclassrooms.realestatemanager.presentation.create_property.CreatePropertyViewModel
import com.openclassrooms.realestatemanager.presentation.detail.DetailAssistedFactory
import com.openclassrooms.realestatemanager.presentation.detail.DetailScreen
import com.openclassrooms.realestatemanager.presentation.home.HomeScreen
import com.openclassrooms.realestatemanager.presentation.home.HomeState
import com.openclassrooms.realestatemanager.presentation.home.HomeViewModel

@Composable
fun Navigation(
    windowSize: WindowSizeType,
    homeViewModel: HomeViewModel,
    createPropertyViewModel: CreatePropertyViewModel,
    state: HomeState,
    modifier: Modifier = Modifier,
    assistedFactory: DetailAssistedFactory,
) {
    val isExpanded = windowSize == WindowSizeType.Expanded

    var index by remember{ mutableStateOf(0) }

    var isItemOpened by remember { mutableStateOf(false) }

    val homeScreenType = getScreenType(isExpanded = isExpanded, isDetailOpened = isItemOpened)

    when(homeScreenType){
        ScreenType.List -> {
            //println("go to List and index is $index")
            HomeScreen(
                state = state,
                modifier = modifier,
                onItemClicked = {
                    //println("index is set to $it")
                    index = it
                    isItemOpened = true
                }
            )
        }
        ScreenType.Detail -> {
            //println("go to detail and index is $index")
            LaunchDetailScreenFromState(
                state = state,
                modifier = modifier,
                assistedFactory = assistedFactory,
                index = index
            ) {
                isItemOpened = false
            }
        }
        ScreenType.ListWithDetail -> {
            println("tablet mode")
            ListAndDetailScreen(
                state = state,
                onItemClicked = {
                    index = it
                },
                assistedFactory = assistedFactory,
                modifier = modifier,
                index = index
            )
        }
    }
}

@Composable
private fun ListAndDetailScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    onItemClicked:(index:Int) -> Unit,
    assistedFactory: DetailAssistedFactory,
    index: Int,
){
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        HomeScreen(state = state, onItemClicked = onItemClicked, modifier = modifier.width(300.dp))
        Divider(
            color = Color.Gray,
            modifier = Modifier.fillMaxHeight().width(1.dp)
        )
        LaunchDetailScreenFromState(
            state = state,
            modifier = modifier,
            assistedFactory = assistedFactory,
            index = index
        ) {

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

@Composable
fun LaunchDetailScreenFromState(
    state: HomeState,
    modifier: Modifier,
    assistedFactory: DetailAssistedFactory,
    index: Int,
    onBackPressed:() -> Unit
){
    when(state.properties){
        is ScreenViewState.Loading -> {
            //println("data loading")
            CircularProgressIndicator()
        }

        is ScreenViewState.Success -> {
            val properties = state.properties.data
            //println("go to data and index is $index")
            DetailScreen(propertyId = properties[index].id , assistedFactory = assistedFactory, modifier = modifier, onBackPressed = onBackPressed)

        }

        is ScreenViewState.Error -> {
            println("error: $state.properties.message")
            Text(
                text = state.properties.message ?: "Unknown Error",
                color = MaterialTheme.colorScheme.error
            )
        }

        else -> {}
    }

}