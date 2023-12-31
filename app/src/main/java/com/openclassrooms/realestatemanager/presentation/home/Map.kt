package com.openclassrooms.realestatemanager.presentation.home


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.common.ScreenViewState
import com.openclassrooms.realestatemanager.common.utils.ImageUtil
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.presentation.navigation.CheckConnectionViewModel
import com.openclassrooms.realestatemanager.presentation.navigation.CurrencyViewModel

/**
 * Composable used for managing the display of the map
 * It will first check the permissions are granted
 * If they aren't a warning screen will be displayed with possible solutions
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapView(
    state:HomeState,
    modifier: Modifier,
    onItemClicked:(Int) -> Unit,
    viewModel: HomeViewModel,
    currencyViewModel: CurrencyViewModel,
    onGoToAppSettingsClicked: () -> Unit,
){
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(key1 = locationPermissions.allPermissionsGranted) {
        if (locationPermissions.allPermissionsGranted) {
            viewModel.getCurrentLocation()
        }
    }
    val isPermissionIgnored = remember { mutableStateOf(false) }

    when {
       locationPermissions.allPermissionsGranted-> {
           val currentLocation = viewModel.currentLocation
           if (currentLocation != null ){
               MapWithProperties(
                   state = state,
                   modifier = modifier,
                   onItemClicked = onItemClicked,
                   currentLatLng = LatLng(currentLocation.latitude, currentLocation.longitude),
                   currencyViewModel = currencyViewModel
               )
           }
       }
        else -> {
            LaunchedEffect(Unit) {
                locationPermissions.launchMultiplePermissionRequest()
            }
            val address = when(state.properties){
                is ScreenViewState.Error -> null
                ScreenViewState.Loading -> null
                is ScreenViewState.Success -> {
                    if(state.properties.data.isNotEmpty()){
                        state.properties.data[0].address
                    }else null
                }
            }
            val lat:Double?
            val lng:Double?
            if (address != null){
                lat = address.latitude
                lng = address.longitude
            }else{
                lat = null
                lng = null
            }
            if (isPermissionIgnored.value){
                MapWithProperties(
                    state = state,
                    modifier = modifier,
                    onItemClicked = onItemClicked,
                    currentLatLng = LatLng(lat ?: 46.4, lng ?: 2.1),
                    currencyViewModel = currencyViewModel,
                    isLocationGranted = false,
                    zoomLevel = if (address != null) 10F else 1F
                )
            }
            else{
                MissingPermissionScreen(
                    onGoToAppSettingsClicked = onGoToAppSettingsClicked,
                    onPermissionIgnoredClicked = { isPermissionIgnored.value = true }
                )
            }
        }
    }
}

/**
 * Composable for displaying the map with the Propeties
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapWithProperties(
    state:HomeState,
    isLocationGranted:Boolean = true,
    modifier: Modifier,
    onItemClicked:(Int) -> Unit,
    currentLatLng: LatLng,
    zoomLevel: Float = 18f,
    currencyViewModel: CurrencyViewModel
){
    var showBottomSheet by remember { mutableStateOf(false) }
    val properties:List<PropertyModel>
    when(state.properties){
        is ScreenViewState.Error -> TODO()
        ScreenViewState.Loading -> TODO()
        is ScreenViewState.Success -> properties = state.properties.data
    }
    val cameraPositionState = rememberCameraPositionState {
       position = CameraPosition.fromLatLngZoom(currentLatLng, zoomLevel)
    }
    var selectedProperty by remember {
        mutableStateOf<PropertyModel?>(null)
    }
    var selectedPropertyIndex by remember {
        mutableIntStateOf(0)
    }
    val checkConnectionViewModel: CheckConnectionViewModel = viewModel()


    if (checkConnectionViewModel.isInternetOn()){
        GoogleMap(
            modifier = modifier
                .fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = isLocationGranted,
                mapToolbarEnabled = false
            ),
            properties = MapProperties(isMyLocationEnabled = isLocationGranted),
        ) {

            properties.forEachIndexed { index, property ->
                val lat = property.address.latitude
                val lng = property.address.longitude
                if (lat != null && lng != null) {
                    val location = LatLng(lat, lng)
                    HouseMarkers(location = location, property = property) { innerProperty ->
                        selectedProperty = innerProperty
                        showBottomSheet = true
                        selectedPropertyIndex = index
                    }
                }
            }
        }
    }else{
        MissingInternetConnection()
    }
    if (showBottomSheet){
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }) {

                if (selectedProperty != null){
                    PropertyItemCard(
                        currencyViewModel = currencyViewModel,
                        property = selectedProperty!!,
                        isSelected = false,
                        isLargeScreen = false,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        onItemClicked.invoke(selectedPropertyIndex)
                        showBottomSheet = false
                    }
                }
        }
    }
}

/**
 * Composable for the house markers that appear on the map
 */
@Composable
fun HouseMarkers(
    location: LatLng,
    property:PropertyModel,
    onItemClicked: (PropertyModel) -> Unit

){
    Marker(
        state = MarkerState(position = location),
        icon = ImageUtil.bitmapDescriptorFromVector(LocalContext.current, if (property.soldDate != null){ R.drawable.property_map_sold_img }else R.drawable.property_map_available_img),
        onClick = {
            onItemClicked.invoke(property)
            false
        }
    )
}

/**
 * Composable for the warning screen when there is no internet
 */
@Composable
fun MissingInternetConnection(

){
    Column(modifier = Modifier.padding(vertical = 120.dp, horizontal = 16.dp)) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Internet connection required",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(4.dp))
        Text("This feature requires an internet connection. Please activate your internet connection, or wait until you have access to the internet.")
    }
}

/**
 * Composable for when the permissions are not granted.
 * It informs the user of why there are needed and what can be done.
 */
@Composable
fun MissingPermissionScreen(
    onGoToAppSettingsClicked: () -> Unit,
    onPermissionIgnoredClicked: () -> Unit
){
        Box(
            Modifier
                .fillMaxSize()
        ) {
            Column(Modifier.padding(vertical = 120.dp, horizontal = 16.dp)) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Location permission required",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(Modifier.height(4.dp))
                Text("This is required in order for the app to take display properties on the map")
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Button(
                    modifier = Modifier
                        .weight(1F)
                        .padding(8.dp),
                    onClick = {
                        onGoToAppSettingsClicked.invoke()
                    }
                ) {
                    Text("Go to settings")
                }
                Button(
                    modifier = Modifier
                        .weight(1F)
                        .padding(8.dp),
                    onClick = {
                        onPermissionIgnoredClicked.invoke()
                    }) {
                    Text("Go to map anyway")
                }
            }
        }

}


