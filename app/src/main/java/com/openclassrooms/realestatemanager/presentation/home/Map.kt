package com.openclassrooms.realestatemanager.presentation.home

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.audiofx.BassBoost
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
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
import com.openclassrooms.realestatemanager.presentation.navigation.CurrencyViewModel

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapView(
    state:HomeState,
    modifier: Modifier,
    onItemClicked:(Int) -> Unit,
    viewModel: HomeViewModel,
    currencyViewModel: CurrencyViewModel
){
    val openMapPermissionDialog = remember { mutableStateOf(false) }

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

    when {
       locationPermissions.allPermissionsGranted -> {
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
            MissingPermissionScreen()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapWithProperties(
    state:HomeState,
    modifier: Modifier,
    onItemClicked:(Int) -> Unit,
    currentLatLng: LatLng,
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
       position = CameraPosition.fromLatLngZoom(currentLatLng, 18f)
    }
    var selectedProperty by remember {
        mutableStateOf<PropertyModel?>(null)
    }
    var selectedPropertyIndex by remember {
        mutableStateOf(0)
    }

    GoogleMap(
        modifier = modifier
            .fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = true, mapToolbarEnabled = false),
        properties = MapProperties(isMyLocationEnabled = true),
    ) {

        properties.forEachIndexed{ index, property ->
            val lat = property.address.latitude
            val lng = property.address.longitude
            if (lat != null && lng != null){
                val location = LatLng(lat, lng)
                HouseMarkers(location = location, property = property){
                    property -> selectedProperty = property
                    showBottomSheet = true
                    selectedPropertyIndex = index
                }
            }
        }
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
fun MissingPermissionScreen(

){
    Box(Modifier
        .fillMaxSize()
    ) {
        Column(Modifier.padding(vertical = 120.dp, horizontal = 16.dp)) {
            Icon(Icons.Default.LocationOn,
                contentDescription = null)
            Spacer(Modifier.height(8.dp))
            Text("Location permission required", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(4.dp))
            Text("This is required in order for the app to take display properties on the map")
        }
        val context = LocalContext.current

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            onClick = {
                val intent =
                    Intent(ACCESS_FINE_LOCATION).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                context.startActivity(intent)
            }) {
            Text("Go to settings")
        }
    }
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
