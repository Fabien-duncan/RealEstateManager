package com.openclassrooms.realestatemanager.presentation.home

import android.content.res.Configuration
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.common.ScreenViewState
import com.openclassrooms.realestatemanager.common.utils.TextUtils
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.data.local.model.PropertyWithAllDetails
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    selectedIndex:Int = -1,
    onItemClicked:(index:Int) -> Unit,
    isLargeScreen: Boolean,
    viewModel: HomeViewModel
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
                    MapView(state = state, modifier = modifier.padding(it),onItemClicked = onItemClicked, viewModel = viewModel)
                }
                else{
                    HomePropertyList(
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

