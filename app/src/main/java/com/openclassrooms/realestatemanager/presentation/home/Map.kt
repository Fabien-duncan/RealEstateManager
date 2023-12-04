package com.openclassrooms.realestatemanager.presentation.home


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.audiofx.BassBoost
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.presentation.navigation.CheckConnectionViewModel
import com.openclassrooms.realestatemanager.presentation.navigation.CurrencyViewModel

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapView(
    state:HomeState,
    modifier: Modifier,
    onItemClicked:(Int) -> Unit,
    viewModel: HomeViewModel,
    currencyViewModel: CurrencyViewModel,
    onGoToAppSettingsClicked: () -> Unit,
){
    val context = LocalContext.current
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
    var isPermissionIgnored = remember { mutableStateOf(false) }

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
                    context = context,
                    onGoToAppSettingsClicked = onGoToAppSettingsClicked,
                    onPermissionIgnoredClicked = { isPermissionIgnored.value = true }
                )
            }
        }
    }
}
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
        mutableStateOf(0)
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
                    HouseMarkers(location = location, property = property) { property ->
                        selectedProperty = property
                        showBottomSheet = true
                        selectedPropertyIndex = index
                    }
                }
            }
        }
    }else{
        println("no internet for map")
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
                    }
                }
        }
    }
}

@Composable
fun HouseMarkers(
    location: LatLng,
    property:PropertyModel,
    onItemClicked: (PropertyModel) -> Unit

){
    Marker(
        state = MarkerState(position = location),
        icon = bitmapDescriptorFromVector(LocalContext.current, if (property.soldDate != null){ R.drawable.property_map_sold_img }else R.drawable.property_map_available_img),
        onClick = {
            onItemClicked.invoke(property)
            false
        }
    )
}

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

@Composable
fun MissingPermissionScreen(
    context: Context,
    onGoToAppSettingsClicked: () -> Unit,
    onPermissionIgnoredClicked: () -> Unit
){
    var isPermissionIgnored = remember {false}
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
                        println("package name: ${context.packageName}")
                        onGoToAppSettingsClicked.invoke()
                    }/*{
                        val uid = android.os.Process.myUid()
                        Log.d("MyApp", "User UID: $uid")
                        val intent =
                            Intent(ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null))
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }*/) {
                    Text("Go to settings")
                }
                Button(
                    modifier = Modifier
                        .weight(1F)
                        .padding(8.dp),
                    onClick = {
                        println("changing ispermissionIgnored to ${!isPermissionIgnored}")
                        onPermissionIgnoredClicked.invoke()
                    }) {
                    Text("Go to map anyway")
                }
            }
        }
    /*}else{
        MapWithProperties(
            state = state,
            modifier = modifier,
            onItemClicked = onItemClicked,
            currentLatLng = currentLatLng,
            currencyViewModel = currencyViewModel
        )
    }*/

}


fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}
