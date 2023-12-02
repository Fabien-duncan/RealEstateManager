package com.openclassrooms.realestatemanager.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.common.ScreenViewState
import com.openclassrooms.realestatemanager.presentation.navigation.CurrencyViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
            println("data loading")
            CircularProgressIndicator()
        }

        is ScreenViewState.Success -> {
            val properties = state.properties.data
            //var isMapView by remember { mutableStateOf(false) }
            println("HomeScreen: got data and selected index is $selectedIndex")
            Scaffold(
                floatingActionButton = {

                    FloatingActionButton(
                        onClick = { viewModel.isMapView = !viewModel.isMapView },
                        containerColor = MaterialTheme.colorScheme.secondary,
                        /*modifier = Modifier.padding(bottom = 24.dp)*/
                    ) {
                        if (viewModel.isMapView) {
                            Icon(
                                imageVector = Icons.Default.List ,
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
            println("error: $state.properties.message")
            Text(
                text = state.properties.message ?: "Unknown Error",
                color = MaterialTheme.colorScheme.error
            )
        }
        else -> {}
    }
}

