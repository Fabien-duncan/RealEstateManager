package com.openclassrooms.realestatemanager.presentation.detail

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.common.utils.TextUtils
import com.openclassrooms.realestatemanager.data.local.model.Address
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.domain.model.AddressModel
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import java.text.SimpleDateFormat

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    propertyId: Long,
    assistedFactory: DetailAssistedFactory,
    isLargeView:Boolean,
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

    DetailScreenView(modifier = modifier, state = state, isLargeView = isLargeView)

    BackHandler {
        onBackPressed.invoke()
    }

}

@Composable
private fun DetailScreenView(
    modifier: Modifier,
    state: DetailSate,
    isLargeView:Boolean
){
    val scrollState = rememberScrollState()
    var photos = state.property?.photos
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Column(modifier = modifier
        .padding(bottom = 0.dp)
        .fillMaxHeight()
        .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween) {

        Text(
            text = "Media",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(8.dp)
        )
        if (photos != null) {
            if(!photos.isEmpty()) {
                LazyRow(modifier = Modifier.padding(4.dp)) {
                    itemsIndexed(photos) { index, photo ->
                        PhotoItem(photo = photo)
                    }
                }
            }else{
                EmptyPhotoList()
            }
        }

        state.property?.let {
            Text(
                text = "${TextUtils.capitaliseFirstLetter(state.property.type.name)} Description",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = it.description,
                modifier = Modifier
                    .fillMaxWidth()
                    //.heightIn(max = 150.dp)
                    .padding(8.dp)
                    /*.verticalScroll(scrollState)*/,
                color = Color.DarkGray,
            )
        }
        if (!isPortrait){
            Row{
                HouseDetails(
                    state = state,
                    modifier = Modifier
                        .width(200.dp)
                        .padding(8.dp),
                    isLargeView = true
                )

                state.property?.let {
                    AddressDetail(
                        address = it.address,
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f),
                        isLargeView = true
                    )
                }
            }
        }
        else{
            HouseDetails(
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                isLargeView = false
            )

            state.property?.let {
                AddressDetail(
                    address = it.address,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.Green),
                    isLargeView = false
                )
            }
        }
        ExtraDetails(
            state = state,
            modifier = Modifier,
            isLargeView = isLargeView,
            isPortrait = isPortrait
        )
    }
}

@Composable
private fun PhotoItem(
    modifier: Modifier = Modifier,
    photo: PropertyPhotosModel
){
    val imageUri = Uri.parse(photo.photoPath)
    val photoDescription =
        if(photo.caption == null) "A photo with no caption"
        else "A photo of ${photo.caption}"
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
            contentDescription = photoDescription,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
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
private fun EmptyPhotoList(){
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
                contentDescription = "No Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(4.dp)
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
@Composable
private fun HouseDetails(
    state: DetailSate,
    modifier: Modifier = Modifier,
    isLargeView:Boolean
){

    if(isLargeView){
        Column(
        ) {
            HouseDetailCard(
                painter = painterResource(id = R.drawable.area_image),
                title = "surface",
                value = "${state.property?.area} m²",
                modifier = modifier
            )
            HouseDetailCard(
                painter = painterResource(id = R.drawable.number_rooms_image),
                title = "No. rooms",
                value = state.property?.rooms.toString(),
                modifier = modifier
            )
            HouseDetailCard(
                painter = painterResource(id = R.drawable.number_bathrooms_image),
                title = "No. bathrooms",
                value = state.property?.bathrooms.toString(),
                modifier = modifier
            )
            HouseDetailCard(
                painter = painterResource(id = R.drawable.number_bedrooms_image),
                title = "No. bedrooms",
                value = state.property?.bedrooms.toString(),
                modifier = modifier
            )
        }
    }
    else{
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            var modifier = modifier
                .weight(1f)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {

                HouseDetailCard(
                    painter = painterResource(id = R.drawable.area_image),
                    title = "surface",
                    value = "${state.property?.area} m²",
                    modifier = modifier
                )
                HouseDetailCard(
                    painter = painterResource(id = R.drawable.number_bathrooms_image),
                    title = "No. bathrooms",
                    value = state.property?.bathrooms.toString(),
                    modifier = modifier
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                HouseDetailCard(
                    painter = painterResource(id = R.drawable.number_rooms_image),
                    title = "No. rooms",
                    value = state.property?.rooms.toString(),
                    modifier = modifier
                )
                HouseDetailCard(
                    painter = painterResource(id = R.drawable.number_bedrooms_image),
                    title = "No. bedrooms",
                    value = state.property?.bedrooms.toString(),
                    modifier = modifier
                )
            }
        }
    }
}
@Composable
private fun HouseDetailCard(
    painter: Painter,
    title:String,
    value:String,
    modifier: Modifier
){
    Box(
        modifier = modifier,
    ) {
        Row {
            Image(painter = painter, contentDescription = title, modifier = Modifier.padding(vertical = 8.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = title)
                Text(text = value, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp))
            }

        }
    }
}
@Composable
private fun AddressDetail(
    modifier: Modifier = Modifier,
    address: AddressModel,
    isLargeView:Boolean
){
    val padding = if(isLargeView) 40.dp else 8.dp
    Row(modifier = Modifier
        .padding(top = 16.dp, start = 8.dp, end = 8.dp)
        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Column() {
            Row {
                Image(painter = painterResource(id = R.drawable.address_image), contentDescription = "address")
                Text(text = "Location")
            }
            Column(Modifier.padding(start = 24.dp)) {
                Text(text = "${address.number} ${address.street}", fontSize = 16.sp, color = Color.DarkGray)
                if (address.extra != null )Text(text = address.extra, fontSize = 16.sp, color = Color.DarkGray)
                Text(text = address.city, fontSize = 16.sp, color = Color.DarkGray)
                Text(text = address.state, fontSize = 16.sp, color = Color.DarkGray)
                Text(text = address.postalCode, fontSize = 16.sp, color = Color.DarkGray)
                Text(text = address.country, fontSize = 16.sp, color = Color.DarkGray)
            }
        }
        Column() {
            AsyncImage(
                model = Uri.parse("https://i.insider.com/5c954296dc67671dc8346930?width=1136&format=jpeg"),
                contentDescription = "map view",
                contentScale = if (isLargeView )ContentScale.FillHeight else ContentScale.FillWidth,
                modifier = if (isLargeView) Modifier
                    .heightIn(max = 250.dp)
                    .padding(horizontal = padding, vertical = 8.dp)
                    .border(width = 2.dp, color = MaterialTheme.colorScheme.secondary)
                else Modifier
                    .fillMaxWidth()
                    .padding(horizontal = padding, vertical = 8.dp)
                    .border(width = 2.dp, color = MaterialTheme.colorScheme.secondary),
            )
        }
    }
}
@Composable
private fun ExtraDetails(
    modifier: Modifier = Modifier,
    state: DetailSate,
    isLargeView:Boolean,
    isPortrait: Boolean = false
) {
    val dateFormat = SimpleDateFormat("dd/MM/yy")
    val numberOfColumns = if (isLargeView && !isPortrait) 5 else 3

    Text(
        text = "Nearby Amenities",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.DarkGray,
        modifier = Modifier.padding(8.dp)
    )

    if (state.property?.nearbyPlaces != null) {
        LazyVerticalGrid(
            modifier = Modifier
                .heightIn(min = 50.dp, max = 250.dp)
                .padding(8.dp),
            columns = GridCells.Fixed(numberOfColumns),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(state.property.nearbyPlaces.size) { index ->
                NearbyPlacesCells(TextUtils.capitaliseFirstLetter(state.property.nearbyPlaces[index].name))
            }
        }
    }else{
        Text(
            text = "There are no Amenities added to this property yet!!",
            color = Color.Red,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center
        )
    }

    Column(modifier = Modifier.background(color = Color.LightGray)) {
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.agent_24),
                    contentDescription = "Name of Agent"
                )
                state.property?.let { Text(text = it.agentName) }
            }
            Row() {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Name of Agent")
                if (isLargeView) {
                    Text(text = " Creation: ", fontWeight = FontWeight.Bold)
                    state.property?.let { Text(text = dateFormat.format(it.createdDate)) }
                    state.property?.soldDate?.let {
                        Text(text = " Sold: ", fontWeight = FontWeight.Bold)
                        Text(text = dateFormat.format(it) )
                    }
                }else{
                    Column {
                        Row {
                            Text(text = " Creation: ", fontWeight = FontWeight.Bold)
                            state.property?.let { Text(text = dateFormat.format(it.createdDate)) }
                        }
                        Row {
                            state.property?.soldDate?.let {
                                Text(text = " Sold: ", fontWeight = FontWeight.Bold)
                                Text(text = dateFormat.format(it) )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun NearbyPlacesCells(text: String) {
    Text(text = text)
}