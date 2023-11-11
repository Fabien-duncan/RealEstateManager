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
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.style.TextOverflow
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
    propertyId: Long = -1L,
    isLargeView:Boolean,
    onCreatedClicked:(Long) -> Unit,
    onBackPressed:() -> Unit
) {
    println("in addEdit Screen and the property id is $propertyId")

    AddEditView(modifier = modifier, propertyId = propertyId, isLargeView = isLargeView, onCreatedClicked = onCreatedClicked)

    BackHandler {
        onBackPressed.invoke()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditView(
    modifier: Modifier,
    propertyId: Long,
    onCreatedClicked:(Long) -> Unit,
    isLargeView:Boolean
){
    val addEditViewModel: AddEditViewModel = viewModel()
    if (propertyId > 0) addEditViewModel.getPropertyById(propertyId)
    val state = addEditViewModel.state

    val scrollState = rememberScrollState()
    //var photos = state.photos
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    var price by remember { mutableStateOf(state.price) }

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
                value = "${ state.price ?: "" }",
                onValueChange = {
                    addEditViewModel.onPriceChange(it)
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
            value = state.description ?: "",
            onValueChange = {
                addEditViewModel.onDescriptionChange(it)
                isFormValid = addEditViewModel.isFormValid
            },
            placeholder = { Text(text = "Enter the description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        if (!isPortrait){
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Column(modifier = Modifier.weight(0.5f)){
                    HouseDetails(
                        //state = state,
                        modifier = Modifier
                            .padding(8.dp),
                        isLargeView = true,
                        onAreaChanged = addEditViewModel::onAreaChange,
                        onBathroomsChanged = addEditViewModel::onBathroomsChange,
                        onBedroomsChanged = addEditViewModel::onBedroomsChange,
                        onRoomsChanged = addEditViewModel::onRoomsChange,
                        isFormValid = { isFormValid = addEditViewModel.isFormValid },
                        state = state
                    )
                }

                Column(modifier = Modifier
                    .weight(1f)
                    .height(350.dp)
                    .border(width = 1.dp, color = Color.Gray)){
                    Text(
                        text = "Address",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    AddressDetail(
                        //address = it.address,
                        modifier = Modifier
                            .padding(8.dp),
                        isLargeView = true,
                        onNumberChanged = addEditViewModel::onNumberChange,
                        onStreetChanged = addEditViewModel::onStreetChange,
                        onExtraChanged = addEditViewModel::onExtraChange,
                        onCityChanged = addEditViewModel::onCityChange,
                        onStateChanged = addEditViewModel::onStateChange,
                        onCountryChanged = addEditViewModel::onCountryChange,
                        onPostCodeChanged = addEditViewModel::onPostalCodeChange,
                        isFormValid = { isFormValid = addEditViewModel.isFormValid },
                        state = state
                    )
                }
                Column(modifier = Modifier.weight(0.8f)){
                    AddressMapImage(isLargeView = true)
                }
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
                isFormValid = {isFormValid = addEditViewModel.isFormValid},
                state = state
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
                isFormValid = {isFormValid = addEditViewModel.isFormValid},
                state = state
            )
            AddressMapImage(isLargeView = false)
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
                    color = MaterialTheme.colorScheme.error,
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
                },
                modifier = Modifier
                    .padding(8.dp),
                enabled = isFormValid
            ) {
                Text(text = if (propertyId > 0) "Save Changes" else "Create")
            }
        }
        LaunchedEffect(isAddOrUpdatePropertyFinished) {
            if (isAddOrUpdatePropertyFinished) {
                println("Creating Property")
                addEditViewModel.resetFinishedState()
                if (state.id !=null)onCreatedClicked.invoke(state.id)
                else onCreatedClicked.invoke(-1L)
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
    onAreaChanged: (String?) -> Unit,
    onRoomsChanged: (String?) -> Unit,
    onBedroomsChanged: (String?) -> Unit,
    onBathroomsChanged: (String?) -> Unit,
    isFormValid: () -> Unit,
    state: AddEditState,
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
                isFormValid =isFormValid,
                value = state.rooms
            )
            HouseDetailCard(
                painter = painterResource(id = R.drawable.number_rooms_image),
                title = "No. rooms",
                modifier = modifier,
                onValueChanged = onRoomsChanged,
                isFormValid =isFormValid,
                value = state.rooms
            )
            HouseDetailCard(
                painter = painterResource(id = R.drawable.number_bathrooms_image),
                title = "No. bathrooms",
                modifier = modifier,
                onValueChanged = onBathroomsChanged,
                isFormValid =isFormValid,
                value = state.bathrooms
            )
            HouseDetailCard(
                painter = painterResource(id = R.drawable.number_bedrooms_image),
                title = "No. bedrooms",
                modifier = modifier,
                onValueChanged = onBedroomsChanged,
                isFormValid =isFormValid,
                value = state.bedrooms
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
                    isFormValid =isFormValid,
                    value = state.area

                )
                HouseDetailCard(
                    painter = painterResource(id = R.drawable.number_bathrooms_image),
                    title = "No. bathrooms",
                    modifier = modifier,
                    onValueChanged = onBathroomsChanged,
                    isFormValid =isFormValid,
                    value = state.bathrooms
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
                    isFormValid =isFormValid,
                    value = state.rooms
                )
                HouseDetailCard(
                    painter = painterResource(id = R.drawable.number_bedrooms_image),
                    title = "No. bedrooms",
                    modifier = modifier,
                    onValueChanged = onBedroomsChanged,
                    isFormValid =isFormValid,
                    value = state.bedrooms
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
    onValueChanged: (String?) -> Unit,
    value: Int?,
    isFormValid: () -> Unit
){
    Box(
        modifier = modifier.padding(top = 8.dp),
    ) {
        Row {
            Image(painter = painter, contentDescription = title, modifier = Modifier.padding(top=14.dp))
            Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                OutlinedTextField(
                    value = ("${ value ?: "" }"),
                    onValueChange = {
                        onValueChanged.invoke(it)
                        isFormValid.invoke()
                    },
                    placeholder = { Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    isError = value == null,
                )
            }

        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddressDetail(
    state: AddEditState,
    modifier: Modifier = Modifier,
    //address: AddressModel,
    isLargeView:Boolean,
    isFormValid: () -> Unit,
    onNumberChanged: (String?) -> Unit,
    onStreetChanged: (String) -> Unit,
    onExtraChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onStateChanged: (String) -> Unit,
    onCountryChanged: (String) -> Unit,
    onPostCodeChanged: (String) -> Unit,

){
    val padding = if(isLargeView) 8.dp else 8.dp

    /*var number by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var extra by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }*/

    Row(
        modifier = Modifier
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {

        Column(modifier = Modifier.weight(1f)) {
            Column() {
                OutlinedTextField(
                    value = ("${ state.number ?: "" }"),
                    onValueChange = {
                        onNumberChanged.invoke(it)
                        isFormValid.invoke()
                    },
                    placeholder = { Text(text = "number") },
                    modifier = Modifier.padding(4.dp)
                )
                OutlinedTextField(
                    value = state.extra ?: "",
                    onValueChange = {
                        onExtraChanged.invoke(it)
                    },
                    placeholder = { Text(text = "extra") },
                    modifier = Modifier.padding(4.dp)
                )
                OutlinedTextField(
                    value = state.state ?: "",
                    onValueChange = {
                        onStateChanged.invoke(it)
                        isFormValid.invoke()
                    },
                    //label = { Text(text = "type") },
                    placeholder = { Text(text = "state") },
                    modifier = Modifier.padding(4.dp)
                )
                OutlinedTextField(
                    value = state.postalCode ?: "",
                    onValueChange = {
                        onPostCodeChanged.invoke(it)
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
                value = state.street ?: "",
                onValueChange = {
                    onStreetChanged.invoke(it)
                    isFormValid.invoke()
                },
                //label = { Text(text = "type") },
                placeholder = { Text(text = "street") },
                modifier = Modifier.padding(4.dp)
            )
            OutlinedTextField(
                value = state.city ?: "",
                onValueChange = {
                    onCityChanged.invoke(it)
                    isFormValid.invoke()
                },
                //label = { Text(text = "type") },
                placeholder = { Text(text = "city") },
                modifier = Modifier.padding(4.dp)
            )
            OutlinedTextField(
                value = state.country ?: "",
                onValueChange = {
                    onCountryChanged.invoke(it)
                    isFormValid.invoke()
                },
                //label = { Text(text = "type") },
                placeholder = { Text(text = "country") },
                modifier = Modifier.padding(4.dp)
            )
        }
    }
    /*AsyncImage(
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
    )*/
}
@Composable
private fun AddressMapImage(
    isLargeView:Boolean,
){
    val padding = if(isLargeView) 8.dp else 8.dp
    AsyncImage(
        model = Uri.parse("https://i.insider.com/5c954296dc67671dc8346930?width=1136&format=jpeg"),
        contentDescription = "map view",
        contentScale = if (isLargeView )ContentScale.FillHeight else ContentScale.FillWidth,
        modifier = if (isLargeView) Modifier
            .heightIn(max = 350.dp)
            .padding(horizontal = padding, vertical = 0.dp)
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
    val numberOfColumns = if (!isPortrait) 5 else 2
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
