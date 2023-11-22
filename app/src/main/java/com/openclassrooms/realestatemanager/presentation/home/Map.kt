package com.openclassrooms.realestatemanager.presentation.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.common.ScreenViewState
import com.openclassrooms.realestatemanager.common.utils.BitmapDescriptorUtils
import com.openclassrooms.realestatemanager.common.utils.TextUtils
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MapView(
    state:HomeState,
    modifier: Modifier,
    onItemClicked:(Int) -> Unit
){
    val properties:List<PropertyModel>
    when(state.properties){
        is ScreenViewState.Error -> TODO()
        ScreenViewState.Loading -> TODO()
        is ScreenViewState.Success -> properties = state.properties.data
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(43.63079663665069, 6.66194472598945), 18f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(zoomControlsEnabled = false)
    ) {

        Marker(state = MarkerState(position = LatLng(43.63079663665069, 6.66194472598945)))

        properties.forEachIndexed{ index, property ->
            val lat = property.address.latitude
            val lng = property.address.longitude
            if (lat != null && lng != null){
                val location = LatLng(lat, lng)
                HouseMarkers(location = location, property = property){
                    onItemClicked.invoke(index)
                }
            }
        }
    }
}

@Composable
fun HouseMarkers(
    location: LatLng,
    property:PropertyModel,
    onItemClicked: () -> Unit
){
   /* val imageState = remember{ mutableStateOf<BitmapDescriptor?>(null) }

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit, block = {
        imageState.value = loadBitmapDescriptorFromUrl(context, "https://images.pexels.com/photos/268533/pexels-photo-268533.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
    })*/

    val image = if (!property.photos.isNullOrEmpty()) Uri.parse(property.photos[0].photoPath) else null
    val request = ImageRequest.Builder(LocalContext.current).data(image).allowHardware(false).build()
    MarkerInfoWindow(
        state = MarkerState(position = location),
        icon = bitmapDescriptorFromVector(LocalContext.current, R.drawable.property_map_img),
        onInfoWindowClick = { onItemClicked.invoke() }
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                .padding(12.dp)
                .clip(MaterialTheme.shapes.small)
                .border(1.dp, Color.Gray, MaterialTheme.shapes.small),
        ) {

            Row(modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(0.5f),


                ) {
                    if (image != null) {
                        AsyncImage(
                            model = request,
                            contentDescription = "",
                            contentScale = ContentScale.FillHeight
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
suspend fun loadBitmapFromUrl(context: Context, imageUrl: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .build()

            when (val result = request.context.imageLoader.execute(request)) {
                is SuccessResult -> result.drawable.toBitmap()
                else -> null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
suspend fun loadBitmapDescriptorFromUrl(context: Context, imageUrl: String): BitmapDescriptor? {
    return withContext(Dispatchers.IO) {
        // Use Coil to load the image
        val imageLoader = ImageLoader.Builder(context)
            .crossfade(true)
            .build()

        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .build()

        when (val result = imageLoader.execute(request)) {
            is SuccessResult -> {
                // Convert Coil's Drawable to a Bitmap
                val drawable = result.drawable
                val bitmap = (drawable as? BitmapDrawable)?.bitmap

                // Create a BitmapDescriptor from the Bitmap
                return@withContext if (bitmap!= null)BitmapDescriptorFactory.fromBitmap(bitmap) else null
            }
            else -> throw IllegalArgumentException("Failed to load image from $imageUrl")
        }
    }
}
suspend fun loadImageBitmapFromUrl(context: Context, imageUrl: String): ImageBitmap {
    // Create an ImageLoader
    val imageLoader = ImageLoader.Builder(context)
        .crossfade(true)
        .build()

    // Create an ImageRequest with the given URL
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .build()

    // Execute the request and handle the result
    return when (val result = imageLoader.execute(request)) {
        is SuccessResult -> {
            // Convert Coil's Drawable to a Bitmap
            val drawable = result.drawable
            val bitmap = (drawable.toBitmap()) // Extension function to convert to Bitmap

            // Convert the Bitmap to ImageBitmap
            bitmap.asImageBitmap()
        }
        else -> throw IllegalArgumentException("Failed to load image from $imageUrl")
    }
}

// Extension function to convert Drawable to Bitmap
fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) {
        return bitmap
    }

    val bitmap = Bitmap.createBitmap(
        intrinsicWidth.coerceAtLeast(1),
        intrinsicHeight.coerceAtLeast(1),
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}
