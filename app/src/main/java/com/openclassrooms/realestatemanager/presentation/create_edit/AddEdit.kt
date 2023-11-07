package com.openclassrooms.realestatemanager.presentation.create_edit

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.common.utils.TextUtils
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import com.openclassrooms.realestatemanager.enums.PropertyType
import java.nio.file.WatchEvent

@Composable
fun AddEditScreen(
    modifier: Modifier = Modifier,
    propertyId: Long = -1,
    isLargeView:Boolean,
    onCreatedClicked:() -> Unit,
    onBackPressed:() -> Unit
) {
    println("in Detail Screen and the property id is $propertyId")

    AddEditView(modifier = modifier, isLargeView = isLargeView, onCreatedClicked = onCreatedClicked)

    BackHandler {
        onBackPressed.invoke()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditView(
    modifier: Modifier,
    onCreatedClicked:() -> Unit,
    isLargeView:Boolean
){
    val addEditViewModel: AddEditViewModel = viewModel()
    val state = addEditViewModel.state

    val scrollState = rememberScrollState()
    //var photos = state.photos
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    var type by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    var isTypePickerExpanded by remember { mutableStateOf(false) }
    var onTypeSelected by remember { mutableStateOf(PropertyType.HOUSE)    }
    var textfieldSize by remember { mutableStateOf(Size.Zero)}

    var isFormValid by remember { mutableStateOf(false) }

    val isAddOrUpdatePropertyFinished by rememberUpdatedState(addEditViewModel.isAddOrUpdatePropertyFinished)

    val icon = if (isTypePickerExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    Column(
        modifier = modifier
            .padding(bottom = 0.dp)
            .fillMaxHeight()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
        ){
            OutlinedTextField(
                value = price,
                onValueChange = {
                    price = it
                    addEditViewModel.onPriceChange(it.toDouble())
                    isFormValid = addEditViewModel.isFormValid
                },
                placeholder = { Text(text = "price in $") },
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.dollar_image),
                        contentDescription = "dollar"
                    )
                }
            )
            OutlinedTextField(
                value = state.agentName ?: "",
                onValueChange = {
                    addEditViewModel.onAgentNameChange(it)
                    isFormValid = addEditViewModel.isFormValid
                },
                placeholder = { Text(text = "Agent Name") },
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.agent_24),
                        contentDescription = "Agent"
                    )
                }
            )
        }


        Text(
            text = "Media",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(8.dp)
        )
        /*if (photos != null) {
            if(!photos.isEmpty()) {
                LazyRow(modifier = Modifier.padding(4.dp)) {
                    itemsIndexed(photos) { index, photo ->
                        PhotoItem(photo = photo)
                    }
                }
            }else{
                EmptyPhotoList()
            }
        }*/
        Text(
            text = "Description",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier
                .padding(8.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            OutlinedTextField(
                value = onTypeSelected.name,
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textfieldSize = coordinates.size.toSize()
                    },
                trailingIcon = {
                    Icon(
                        icon, "selection arrow",
                        Modifier.clickable { isTypePickerExpanded = !isTypePickerExpanded },
                    )
                }
            )
            DropdownMenu(
                expanded = isTypePickerExpanded,
                onDismissRequest = { isTypePickerExpanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
                    /*.border(border = BorderStroke(width = 1.dp, color = Color.DarkGray))*/
                    .background(Color.LightGray)
            ) {
                PropertyType.values().forEach { type ->
                    DropdownMenuItem(
                        onClick = {
                            onTypeSelected = type
                            isTypePickerExpanded = false
                            addEditViewModel.onTypeChange(type)
                            isFormValid = addEditViewModel.isFormValid
                        },
                        text = {Text(text = type.name)}

                    )
                }
            }
        }

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
                addEditViewModel.onDescriptionChange(description)
                isFormValid = addEditViewModel.isFormValid
            },
            placeholder = { Text(text = "Enter the description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        if (!isPortrait){
            Row{
                HouseDetails(
                    //state = state,
                    modifier = Modifier
                        .width(200.dp)
                        .padding(8.dp),
                    isLargeView = true,
                    onAreaChanged = addEditViewModel::onAreaChange,
                    onBathroomsChanged = addEditViewModel::onBathroomsChange,
                    onBedroomsChanged = addEditViewModel::onBedroomsChange,
                    onRoomsChanged = addEditViewModel::onRoomsChange,
                    isFormValid = {isFormValid = addEditViewModel.isFormValid}
                )

                AddressDetail(
                    //address = it.address,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    isLargeView = true,
                    onNumberChanged = addEditViewModel::onNumberChange,
                    onStreetChanged = addEditViewModel::onStreetChange,
                    onExtraChanged = addEditViewModel::onExtraChange,
                    onCityChanged = addEditViewModel::onCityChange,
                    onStateChanged = addEditViewModel::onStateChange,
                    onCountryChanged = addEditViewModel::onCountryChange,
                    onPostCodeChanged = addEditViewModel::onPostalCodeChange,
                    isFormValid = {isFormValid = addEditViewModel.isFormValid}
                )
            }
        }
        else{
            HouseDetails(
                //state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                isLargeView = false,
                onAreaChanged = addEditViewModel::onAreaChange,
                onBathroomsChanged = addEditViewModel::onBathroomsChange,
                onBedroomsChanged = addEditViewModel::onBedroomsChange,
                onRoomsChanged = addEditViewModel::onRoomsChange,
                isFormValid = {isFormValid = addEditViewModel.isFormValid}
            )

            Text(
                text = "Address",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                modifier = Modifier.padding(8.dp)
            )

            AddressDetail(
                //address = it.address,
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.Green),
                isLargeView = false,
                onNumberChanged = addEditViewModel::onNumberChange,
                onStreetChanged = addEditViewModel::onStreetChange,
                onExtraChanged = addEditViewModel::onExtraChange,
                onCityChanged = addEditViewModel::onCityChange,
                onStateChanged = addEditViewModel::onStateChange,
                onCountryChanged = addEditViewModel::onCountryChange,
                onPostCodeChanged = addEditViewModel::onPostalCodeChange,
                isFormValid = {isFormValid = addEditViewModel.isFormValid}
            )
        }

        Text(
            text = "Nearby Amenities",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(8.dp)
        )
        NearbyAmenities(isLargeView = isLargeView, isPortrait = isPortrait, addEditState = state, onNearbyPlaceChanged = addEditViewModel::onNearbyPlacesChanged)
        Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
            if (!isFormValid){
                Text(
                    text = "You need to fill in all the required fields",
                    fontSize = 14.sp,
                    color = Color.Red,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                )
            }
            Button(
                onClick = {
                    addEditViewModel.addOrUpdateProperty()
                    /*onCreatedClicked.invoke()
                    println("creating Property")*/
                },
                modifier = Modifier
                    .padding(8.dp),
                enabled = isFormValid
            ) {
                Text(text = "Create")
            }
        }
        LaunchedEffect(isAddOrUpdatePropertyFinished) {
            if (isAddOrUpdatePropertyFinished) {
                println("Creating Property")
                onCreatedClicked.invoke()
            }
        }
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
            contentScale = ContentScale.FillHeight,
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
    //state: DetailSate,
    modifier: Modifier = Modifier,
    onAreaChanged: (Int) -> Unit,
    onRoomsChanged: (Int) -> Unit,
    onBedroomsChanged: (Int) -> Unit,
    onBathroomsChanged: (Int) -> Unit,
    isFormValid: () -> Unit,
    isLargeView:Boolean
){

    if(isLargeView){
        Column(
        ) {
            HouseDetailCard(
                painter = painterResource(id = R.drawable.area_image),
                title = "surface in m²",
                modifier = modifier,
                onValueChanged = onAreaChanged,
                isFormValid =isFormValid
            )
            HouseDetailCard(
                painter = painterResource(id = R.drawable.number_rooms_image),
                title = "No. rooms",
                modifier = modifier,
                onValueChanged = onRoomsChanged,
                isFormValid =isFormValid
            )
            HouseDetailCard(
                painter = painterResource(id = R.drawable.number_bathrooms_image),
                title = "No. bathrooms",
                modifier = modifier,
                onValueChanged = onBathroomsChanged,
                isFormValid =isFormValid
            )
            HouseDetailCard(
                painter = painterResource(id = R.drawable.number_bedrooms_image),
                title = "No. bedrooms",
                modifier = modifier,
                onValueChanged = onBedroomsChanged,
                isFormValid =isFormValid
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
                    title = "surface in m²",
                    modifier = modifier,
                    onValueChanged = onAreaChanged,
                    isFormValid =isFormValid

                )
                HouseDetailCard(
                    painter = painterResource(id = R.drawable.number_bathrooms_image),
                    title = "No. bathrooms",
                    modifier = modifier,
                    onValueChanged = onBathroomsChanged,
                    isFormValid =isFormValid
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                HouseDetailCard(
                    painter = painterResource(id = R.drawable.number_rooms_image),
                    title = "No. rooms",
                    modifier = modifier,
                    onValueChanged = onRoomsChanged,
                    isFormValid =isFormValid
                )
                HouseDetailCard(
                    painter = painterResource(id = R.drawable.number_bedrooms_image),
                    title = "No. bedrooms",
                    modifier = modifier,
                    onValueChanged = onBedroomsChanged,
                    isFormValid =isFormValid
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HouseDetailCard(
    painter: Painter,
    title:String,
    modifier: Modifier,
    onValueChanged: (Int) -> Unit,
    isFormValid: () -> Unit
){
    var value by remember { mutableStateOf("") }
    Box(
        modifier = modifier.padding(top = 8.dp),
    ) {
        Row {
            Image(painter = painter, contentDescription = title, modifier = Modifier.padding(top=14.dp))
            Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                OutlinedTextField(
                    value = value,
                    onValueChange = {
                        value = it
                        onValueChanged.invoke(value.toInt())
                        isFormValid.invoke()
                    },
                    placeholder = { Text(text = title) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                )
            }

        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddressDetail(
    modifier: Modifier = Modifier,
    //address: AddressModel,
    isLargeView:Boolean,
    isFormValid: () -> Unit,
    onNumberChanged: (Int) -> Unit,
    onStreetChanged: (String) -> Unit,
    onExtraChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onStateChanged: (String) -> Unit,
    onCountryChanged: (String) -> Unit,
    onPostCodeChanged: (String) -> Unit,

){
    val padding = if(isLargeView) 40.dp else 8.dp

    var number by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var extra by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {

        Column(modifier = Modifier.weight(1f)) {
            Column() {
                OutlinedTextField(
                    value = number,
                    onValueChange = {
                        number = it
                        onNumberChanged.invoke(number.toInt())
                        isFormValid.invoke()
                    },
                    //label = { Text(text = "type") },
                    placeholder = { Text(text = "number") },
                    modifier = Modifier.padding(4.dp)
                )
                OutlinedTextField(
                    value = extra,
                    onValueChange = {
                        extra = it
                        onExtraChanged.invoke(extra)
                    },
                    //label = { Text(text = "type") },
                    placeholder = { Text(text = "extra") },
                    modifier = Modifier.padding(4.dp)
                )
                OutlinedTextField(
                    value = state,
                    onValueChange = {
                        state = it
                        onStateChanged.invoke(state)
                        isFormValid.invoke()
                    },
                    //label = { Text(text = "type") },
                    placeholder = { Text(text = "state") },
                    modifier = Modifier.padding(4.dp)
                )
                OutlinedTextField(
                    value = postalCode,
                    onValueChange = {
                        postalCode = it
                        onPostCodeChanged.invoke(postalCode)
                        isFormValid.invoke()
                    },
                    //label = { Text(text = "type") },
                    placeholder = { Text(text = "post code") },
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            OutlinedTextField(
                value = street,
                onValueChange = {
                    street = it
                    onStreetChanged.invoke(street)
                    isFormValid.invoke()
                },
                //label = { Text(text = "type") },
                placeholder = { Text(text = "street") },
                modifier = Modifier.padding(4.dp)
            )
            OutlinedTextField(
                value = city,
                onValueChange = {
                    city = it
                    onCityChanged.invoke(city)
                    isFormValid.invoke()
                },
                //label = { Text(text = "type") },
                placeholder = { Text(text = "city") },
                modifier = Modifier.padding(4.dp)
            )
            OutlinedTextField(
                value = country,
                onValueChange = {
                    country = it
                    onCountryChanged.invoke(country)
                    isFormValid.invoke()
                },
                //label = { Text(text = "type") },
                placeholder = { Text(text = "country") },
                modifier = Modifier.padding(4.dp)
            )
        }
    }
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
@Composable
private fun NearbyAmenities(
    isLargeView:Boolean,
    isPortrait: Boolean = false,
    addEditState: AddEditState,
    onNearbyPlaceChanged: (NearbyPlacesType) -> Unit,
){
    val numberOfColumns = if (isLargeView && !isPortrait) 5 else 2
    LazyVerticalGrid(
        modifier = Modifier
            .heightIn(min = 50.dp, max = 250.dp)
            .padding(8.dp),
        columns = GridCells.Fixed(numberOfColumns),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(NearbyPlacesType.values()){ nearbyType ->
            NearbyCheckBox(nearbyPlacesType = nearbyType, addEditState = addEditState, onNearbyPlaceChanged = onNearbyPlaceChanged)
        }
    }
}
@Composable
private fun NearbyCheckBox(
    nearbyPlacesType: NearbyPlacesType,
    addEditState: AddEditState,
    onNearbyPlaceChanged: (NearbyPlacesType) -> Unit,
){
    val isTicked = if (addEditState.nearbyPlaces != null) addEditState.nearbyPlaces.contains(nearbyPlacesType) else false

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isTicked,
            onCheckedChange = {
                onNearbyPlaceChanged.invoke(nearbyPlacesType)
            }
        )
        Text(text = TextUtils.capitaliseFirstLetter(nearbyPlacesType.displayText))
    }
}
