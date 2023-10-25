package com.openclassrooms.realestatemanager.presentation.detail

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.common.utils.TextUtils
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    propertyId: Long,
    assistedFactory: DetailAssistedFactory,
    onBackPressed:() -> Unit
) {
    println("in Detail Screen and the property id is $propertyId")
    val viewModel = viewModel(
        modelClass = DetailViewModel::class.java,
        factory = DetailedViewModelFactory(
            propertyId = propertyId,
            assistedFactory = assistedFactory
        )
    )
    viewModel.updatePropertyById(propertyId)
    val state = viewModel.state

    println("property id is ${state.property?.id}")

    DetailScreenView(modifier = modifier, state = state)

    BackHandler {

        onBackPressed.invoke()
    }

}

@Composable
private fun DetailScreenView(
    modifier: Modifier,
    state: DetailSate,
){
    var photos = listOf<PropertyPhotosModel>(
        PropertyPhotosModel("https://images.pexels.com/photos/268533/pexels-photo-268533.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "view"),
        PropertyPhotosModel("https://www.mydomaine.com/thmb/CaWdFGvTH4-h1VvG6tukpKuU2lM=/3409x0/filters:no_upscale():strip_icc()/binary-4--583f06853df78c6f6a9e0b7a.jpeg", "Facade"),
        PropertyPhotosModel("https://designingidea.com/wp-content/uploads/2022/01/modern-home-types-of-room-living-space-dining-area-kitchen-glass-door-floor-rug-loft-pendant-light-ss.jpg", null),
        PropertyPhotosModel("https://foyr.com/learn/wp-content/uploads/2022/05/foyer-or-entry-hall-types-of-rooms-in-a-house-1024x819.jpg", "entrance")
        )
    //var photos = listOf<PropertyPhotosModel>()
    Column(modifier = modifier.fillMaxSize()) {

        Text(
            text = "Media",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(8.dp)
        )
        if(!photos.isEmpty()) {
            LazyRow() {
                itemsIndexed(photos) { index, photo ->
                    PhotoItem(photo = photo)
                }
            }
        }else{
            EmptyPhotoList()
        }



        Text(text = "Surface Area: ${state.property?.area}")
        Text(text = "Number of Rooms: ${state.property?.rooms}")
        Text(text = "Number of bathrooms ${state.property?.bathrooms}")
        Text(text = "Number of bedrooms: ${state.property?.bedrooms}")
    }
}

@Composable
fun PhotoItem(
    modifier: Modifier = Modifier,
    photo: PropertyPhotosModel
){
    val imageUri = Uri.parse(photo.photoPath)

    Box(
        modifier = Modifier
            .padding(4.dp)
            .width(120.dp)
            .height(120.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = "A photo",
            contentScale = ContentScale.FillHeight,
            modifier = modifier.fillMaxSize()
        )

        photo.caption?.let { caption ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .height(30.dp)
                    .align(Alignment.BottomStart),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = TextUtils.capitaliseFirstLetter(caption),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
@Composable
fun EmptyPhotoList(){
    Box(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(120.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(MaterialTheme.colorScheme.secondary)
    ){
        Column() {
            Image(
                painter = painterResource(id = R.drawable.missing_image),
                contentDescription = null, // Set content description if needed
                modifier = Modifier.fillMaxWidth().height(80.dp).padding(4.dp)
            )
            Text(
                text = "There are no Images for this Property",
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}