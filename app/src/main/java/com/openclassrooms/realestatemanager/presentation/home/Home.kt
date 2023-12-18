package com.openclassrooms.realestatemanager.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.common.ScreenViewState
import com.openclassrooms.realestatemanager.presentation.navigation.CurrencyViewModel

/**
 * Composable used for dealing with the display of the main screen.
 * It will display a List view or the map view
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    selectedIndex:Int = -1,
    onItemClicked:(index:Int) -> Unit,
    isLargeScreen: Boolean,
    viewModel: HomeViewModel,
    onGoToAppSettingsClicked: () -> Unit,
    currencyViewModel: CurrencyViewModel
){
    when(state.properties){
        is ScreenViewState.Loading -> {
            CircularProgressIndicator()
        }

        is ScreenViewState.Success -> {
            val properties = state.properties.data
            Scaffold(
                floatingActionButton = {

                    FloatingActionButton(
                        onClick = { viewModel.isMapView = !viewModel.isMapView },
                        containerColor = MaterialTheme.colorScheme.secondary,
                    ) {
                        if (viewModel.isMapView) {
                            Icon(
                                imageVector = Icons.Default.List ,
                                tint = Color.White,
                                contentDescription = "List View"
                            )
                        }else{
                            Image(
                                painter = painterResource(id = R.drawable.map_image),
                                contentDescription = "Map View"
                            )
                        }
                    }
                }
            ){
                if (viewModel.isMapView){
                    MapView(
                        state = state,
                        modifier = modifier.padding(it),
                        onItemClicked = onItemClicked,
                        viewModel = viewModel,
                        currencyViewModel = currencyViewModel,
                        onGoToAppSettingsClicked = onGoToAppSettingsClicked
                    )
                }
                else{
                    HomePropertyList(
                        currencyViewModel = currencyViewModel,
                        properties = properties,
                        modifier = modifier.padding(it),
                        onItemClicked = onItemClicked,
                        selectedIndex = selectedIndex,
                        isLargeScreen = isLargeScreen
                    )
                }
            }
        }

        is ScreenViewState.Error -> {
            MissingProperties(modifier = modifier)
        }
    }
}

