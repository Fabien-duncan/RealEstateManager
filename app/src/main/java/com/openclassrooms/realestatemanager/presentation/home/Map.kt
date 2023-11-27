package com.openclassrooms.realestatemanager.presentation.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.common.ScreenViewState
import com.openclassrooms.realestatemanager.common.utils.TextUtils
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapView(
    state:HomeState,
    modifier: Modifier,
    onItemClicked:(Int) -> Unit,
    viewModel: HomeViewModel
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



    AnimatedContent(
        targetState = locationPermissions.allPermissionsGranted, label = "Permissions"
    ) { areGranted ->

        if (areGranted) {
            val currentLocation = viewModel.currentLocation
            if (currentLocation != null ){
                MapWithProperties(state = state, modifier = modifier, onItemClicked = onItemClicked, currentLatLng = LatLng(currentLocation.latitude, currentLocation.longitude))
            }

        } else {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(text = "We need location permissions for this application.")
                Button(
                    onClick = { locationPermissions.launchMultiplePermissionRequest() }
                ) {
                    Text(text = "Accept")
                }
            }

        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapWithProperties(
    state:HomeState,
    modifier: Modifier,
    onItemClicked:(Int) -> Unit,
    currentLatLng: LatLng
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
                    BottomSheetPropertyCard(property = selectedProperty!!) {
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
fun BottomSheetPropertyCard(
    property:PropertyModel,
    onItemClicked: () -> Unit
){
    val listOfPhotos = property.photos
    val imageUri = if (!listOfPhotos.isNullOrEmpty()) Uri.parse(listOfPhotos[0].photoPath) else null
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(MaterialTheme.shapes.small)
            .clickable { onItemClicked.invoke() }
            .background(Color.Transparent)
    ) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.Transparent
            )
            .height(120.dp)) {

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(0.5f),


                ) {
                    if (imageUri != null){
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "",
                            placeholder = painterResource(id = R.drawable.missing_image),
                            contentScale = ContentScale.FillHeight,
                            onError = { error -> println("Error Loading Image: $error") },
                            onSuccess = { success -> println("Loaded Image: $success ") },
                            onLoading = { println("Loading Image...") }
                        )
                    }
                    else{
                        Image(
                            painter = painterResource(id = R.drawable.missing_image),
                            contentDescription = "No Image",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondary)
                                .fillMaxSize(),
                        )
                    }

                }
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly


                ) {
                    Text(
                        text = TextUtils.capitaliseFirstLetter("${property.type}"),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = TextUtils.capitaliseFirstLetter("${property.address.number} ${property.address.street}"),
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "\$${property.price}",
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
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