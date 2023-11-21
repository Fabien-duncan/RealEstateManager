package com.openclassrooms.realestatemanager.presentation.create_edit

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Popup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.common.utils.TextUtils
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import com.openclassrooms.realestatemanager.enums.PropertyType
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Objects

@Composable
fun AddEditScreen(
    modifier: Modifier = Modifier,
    propertyId: Long = -1L,
    isLargeView:Boolean,
    addEditViewModel: AddEditViewModel,
    onCreatedClicked:(Long) -> Unit,
    onBackPressed:() -> Unit
) {
    println("in addEdit Screen and the property id is $propertyId")
    //val addEditViewModel: AddEditViewModel = viewModel()
    if (propertyId > 0) addEditViewModel.getPropertyById(propertyId)

    AddEditView(modifier = modifier, propertyId = propertyId, isLargeView = isLargeView, onCreatedClicked = onCreatedClicked, addEditViewModel = addEditViewModel)

    BackHandler {
        addEditViewModel.resetState()
        onBackPressed.invoke()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditView(
    modifier: Modifier,
    propertyId: Long,
    onCreatedClicked:(Long) -> Unit,
    isLargeView:Boolean,
    addEditViewModel: AddEditViewModel
){

    val state = addEditViewModel.state

    println("addEdit  View: state.price is ${state.price}")

    val scrollState = rememberScrollState()

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT


    var isTypePickerExpanded by remember { mutableStateOf(false) }
    var onTypeSelected by remember { mutableStateOf(PropertyType.HOUSE)    }
    var textFieldSize by remember { mutableStateOf(Size.Zero)}

    var isFormValid by remember { mutableStateOf(false) }

    var isImageSelectChoiceVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val photosPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = {uris ->
            addEditViewModel.onImagesAdded(originalImagesUris = uris, context =  context)
        }
    )
    val tempFile = context.createImageFile()
    val uri: Uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider",
        tempFile
    )

    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ){
        if (it) {
            addEditViewModel.onImagesAdded(originalImagesUris = listOf(uri), context =  context)
            println("launch camera: Uri is $uri")
        }
        else{
            println("failed to get uri")
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, launch the camera intent
            takePicture.launch(uri)
            println("camera Image Uri = $uri")
        } else {
            // Handle the case where permission is not granted
            println("Permission Denied ")
        }
    }

    val dateFormat = SimpleDateFormat("dd/MM/yy")
    var openDialog = remember { mutableStateOf(false) }

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


        val addEditProperties = PropertyPhotosModel(id = -1L, photoPath = "", caption = "")//used for creating the button to add add properties
        //combines the list of properties with the add property button
        val photosWithAddButton = if(!state.photos.isNullOrEmpty()) {
            state.photos + addEditProperties
        }else{
            mutableListOf(addEditProperties)
        }
        LazyRow(modifier = Modifier.padding(4.dp)) {
            itemsIndexed(photosWithAddButton) { index, photo ->
                if (photo.id != -1L){
                    PhotoItem(photo = photo, onPhotoChanged = addEditViewModel::onPhotoCaptionChanged, index = index )
                }
                else{
                    AddPhotoItem(onAddPhotoClicked = { isImageSelectChoiceVisible = true })
                    if (isImageSelectChoiceVisible) {
                        PhotoSelectPopup(
                            changeIsImageSelectedChoice = { isImageSelectChoiceVisible = false },
                            onAddPhotoClicked = photosPicker,
                            context = context,
                            uri = uri,
                            takePicture = takePicture,
                            permissionLauncher = permissionLauncher
                        )
                    }
                }
            }
        }

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
                        textFieldSize = coordinates.size.toSize()
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
                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                    /*.border(border = BorderStroke(width = 1.dp, color = Color.DarkGray))*/
                    .background(Color.LightGray)
            ) {
                PropertyType.values().forEach { type ->
                    DropdownMenuItem(
                        onClick = {
                            onTypeSelected = type
                            isTypePickerExpanded = false
                            addEditViewModel.onTypeChange(type)
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
                        state = state
                    )
                }

                Column(modifier = Modifier
                    .weight(1f)
                    .height(380.dp)
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
                        isLargeView = true,
                        onCheckAddressClicked = addEditViewModel::getLatLongFromAddress,
                        onNumberChanged = addEditViewModel::onNumberChange,
                        onStreetChanged = addEditViewModel::onStreetChange,
                        onExtraChanged = addEditViewModel::onExtraChange,
                        onCityChanged = addEditViewModel::onCityChange,
                        onStateChanged = addEditViewModel::onStateChange,
                        onCountryChanged = addEditViewModel::onCountryChange,
                        onPostCodeChanged = addEditViewModel::onPostalCodeChange,
                        state = state
                    )
                }
                Column(modifier = Modifier.weight(0.8f)) {
                    AddressMapImage(
                        isLargeView = true,
                        isAddressValidated = addEditViewModel.isAddressValid,
                        mapImageLink = addEditViewModel.mapImageLink,
                        onIsAddressValidChanged = addEditViewModel::onIsAddressValidChanged
                    )
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
                isLargeView = false,
                onCheckAddressClicked = addEditViewModel::getLatLongFromAddress,
                onNumberChanged = addEditViewModel::onNumberChange,
                onStreetChanged = addEditViewModel::onStreetChange,
                onExtraChanged = addEditViewModel::onExtraChange,
                onCityChanged = addEditViewModel::onCityChange,
                onStateChanged = addEditViewModel::onStateChange,
                onCountryChanged = addEditViewModel::onCountryChange,
                onPostCodeChanged = addEditViewModel::onPostalCodeChange,
                state = state
            )
            AddressMapImage(
                isLargeView = false,
                isAddressValidated = addEditViewModel.isAddressValid,
                mapImageLink = addEditViewModel.mapImageLink,
                onIsAddressValidChanged = addEditViewModel::onIsAddressValidChanged
            )
        }

        Text(
            text = "Nearby Amenities",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(8.dp)
        )
        NearbyAmenities(
            isLargeView = isLargeView,
            isPortrait = isPortrait,
            addEditState = state,
            onNearbyPlaceChanged = {
                addEditViewModel.onNearbyPlacesChanged(it)
            }
        )
        if(state.id > 0){
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.isSold,
                        onCheckedChange = {
                            addEditViewModel.onIsSoldChange()
                        }
                    )
                    Text(text = "Property is sold")
                }
                if (state.isSold){

                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "date",
                        modifier = Modifier.padding(start = 12.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(8.dp)
                            .border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                            .clickable {
                                openDialog.value = true
                            },
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            fontStyle = FontStyle.Normal,
                            text = if(state.soldDate != null )dateFormat.format(state.soldDate) else "Select date",
                            modifier = Modifier
                                .padding(start = 12.dp)

                        )
                    }
                    DatePicker(
                        openDialog = openDialog,
                        onSoldDateChanged = addEditViewModel::onSoldDateChange,
                    )
                }
            }
        }
        Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
            if (!addEditViewModel._isFormValid){
                Text(
                    text = "${if(propertyId > 0) "Nothing has been modified or you" else "you"} need to fill in all the required fields",
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
                enabled = addEditViewModel._isFormValid
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
private fun PhotoSelectPopup(
    changeIsImageSelectedChoice: () -> Unit,
    onAddPhotoClicked: ManagedActivityResultLauncher<PickVisualMediaRequest, List<Uri>>,
    context:Context,
    uri:Uri,
    takePicture: ManagedActivityResultLauncher<Uri, Boolean>,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>
){

    Popup(onDismissRequest = { changeIsImageSelectedChoice.invoke()}, alignment = Alignment.CenterEnd ) {
        Column(){
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    // Handle confirm button click
                    changeIsImageSelectedChoice.invoke()
                    onAddPhotoClicked.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            ) {
                Image(painter = painterResource(id = R.drawable.photo_gallery_24), contentDescription = "from gallery", modifier = Modifier.padding(end = 4.dp))
                Text("from Gallery")

            }
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED)
                    {
                        println("Launching camera ")
                        takePicture.launch(uri)
                    }
                    else
                    {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                    // Handle confirm button click
                    changeIsImageSelectedChoice.invoke()
                }
            ) {
                Image(painter = painterResource(id = R.drawable.photo_camera_24), contentDescription = "from camera", modifier = Modifier.padding(end = 4.dp))
                Text("from Camera")

            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhotoItem(
    modifier: Modifier = Modifier,
    photo: PropertyPhotosModel,
    onPhotoChanged:(String, Int) -> Unit,
    index: Int
){
    val imageUri = Uri.parse(photo.photoPath)

    val photoDescription =
        if(photo.caption == null) "A photo with no caption"
        else "A photo of ${photo.caption}"
    Box(
        modifier = Modifier
            .padding(4.dp)
            .width(160.dp)
            .height(160.dp)
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


        OutlinedTextField(
            value ="${TextUtils.capitaliseFirstLetter(photo.caption ?: "")}" ,
            textStyle = TextStyle(
                color = Color.White,

            ),
            onValueChange = {
                println("change caption")
                onPhotoChanged.invoke(it, index)
            },
            placeholder = { Text(text = "Enter caption", color = Color.White) },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .background(Color.Black.copy(alpha = 0.5f)),
        )
        }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPhotoItem(
    modifier: Modifier = Modifier,
    onAddPhotoClicked: () -> Unit //ManagedActivityResultLauncher<PickVisualMediaRequest, List<Uri>>
){

    Box(
        modifier = Modifier
            .padding(4.dp)
            .width(160.dp)
            .height(160.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable {
                println("adding a photo")
                /*onAddPhotoClicked.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )*/

                onAddPhotoClicked.invoke()
            }
            //.border(width = 2.dp, color = Color.LightGray)
    ) {
        Image(
            painter = painterResource(id = R.drawable.add_image_48),
            contentDescription = "click to add photos",
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
        )
        //Text(text = "Add photos", Modifier.fillMaxWidth().align(Alignment.BottomStart), textAlign = TextAlign.Center)

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
                value = state.rooms
            )
            HouseDetailCard(
                painter = painterResource(id = R.drawable.number_rooms_image),
                title = "No. rooms",
                modifier = modifier,
                onValueChanged = onRoomsChanged,
                value = state.rooms
            )
            HouseDetailCard(
                painter = painterResource(id = R.drawable.number_bathrooms_image),
                title = "No. bathrooms",
                modifier = modifier,
                onValueChanged = onBathroomsChanged,
                value = state.bathrooms
            )
            HouseDetailCard(
                painter = painterResource(id = R.drawable.number_bedrooms_image),
                title = "No. bedrooms",
                modifier = modifier,
                onValueChanged = onBedroomsChanged,
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
                    value = state.area

                )
                HouseDetailCard(
                    painter = painterResource(id = R.drawable.number_bathrooms_image),
                    title = "No. bathrooms",
                    modifier = modifier,
                    onValueChanged = onBathroomsChanged,
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
                    value = state.rooms
                )
                HouseDetailCard(
                    painter = painterResource(id = R.drawable.number_bedrooms_image),
                    title = "No. bedrooms",
                    modifier = modifier,
                    onValueChanged = onBedroomsChanged,
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
    isLargeView:Boolean,
    onCheckAddressClicked:() -> Unit,
    onNumberChanged: (String?) -> Unit,
    onStreetChanged: (String) -> Unit,
    onExtraChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onStateChanged: (String) -> Unit,
    onCountryChanged: (String) -> Unit,
    onPostCodeChanged: (String) -> Unit,

){
    //var isAddressValidated by remember{ mutableStateOf(false) }
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
                    },
                    //label = { Text(text = "type") },
                    placeholder = { Text(text = "state") },
                    modifier = Modifier.padding(4.dp)
                )
                OutlinedTextField(
                    value = state.postalCode ?: "",
                    onValueChange = {
                        onPostCodeChanged.invoke(it)
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
                },
                //label = { Text(text = "type") },
                placeholder = { Text(text = "street") },
                modifier = Modifier.padding(4.dp)
            )
            OutlinedTextField(
                value = state.city ?: "",
                onValueChange = {
                    onCityChanged.invoke(it)
                },
                //label = { Text(text = "type") },
                placeholder = { Text(text = "city") },
                modifier = Modifier.padding(4.dp)
            )
            OutlinedTextField(
                value = state.country ?: "",
                onValueChange = {
                    onCountryChanged.invoke(it)
                },
                //label = { Text(text = "type") },
                placeholder = { Text(text = "country") },
                modifier = Modifier.padding(4.dp)
            )
        }
    }
    Button(onClick = { onCheckAddressClicked.invoke() }, modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Text(text = "Check address")

    }
}
@Composable
private fun AddressMapImage(
    isLargeView:Boolean,
    isAddressValidated: Boolean,
    mapImageLink:String,
    onIsAddressValidChanged: () -> Unit,
){
    if(mapImageLink.isNotEmpty()){
        Box(
            modifier = if (isLargeView) Modifier
                .height(380.dp)
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
                .border(width = 2.dp, color = MaterialTheme.colorScheme.secondary)
            else Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .border(width = 2.dp, color = MaterialTheme.colorScheme.secondary),
        ){
            AsyncImage(
                model = Uri.parse(mapImageLink),
                contentDescription = "map view",
                modifier = if (isLargeView) Modifier
                    .height(380.dp)
                else Modifier
                    .fillMaxWidth(),
                contentScale = if (isLargeView) ContentScale.FillHeight else ContentScale.FillWidth,
            )
            Row(
                modifier = Modifier
                    .height(54.dp)
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "Click to validate Address", fontStyle = FontStyle.Italic)
                IconToggleButton(
                    checked = isAddressValidated,
                    onCheckedChange = { onIsAddressValidChanged.invoke()},
                    modifier = Modifier
                        .size(48.dp)
                        .padding(horizontal = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Radio button icon",
                        tint = if (isAddressValidated) Color.Green else Color.LightGray,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
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
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )

    return image
}

