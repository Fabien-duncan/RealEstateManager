package com.openclassrooms.realestatemanager.presentation.create_edit

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel
import com.openclassrooms.realestatemanager.enums.CurrencyType
import com.openclassrooms.realestatemanager.presentation.navigation.CurrencyViewModel
import com.openclassrooms.realestatemanager.presentation.property_type_picker.PropertyTypePicker
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

/**
 * The composable the manages the add/edit screen
 */
@Composable
fun AddEditScreen(
    currencyViewModel: CurrencyViewModel,
    modifier: Modifier = Modifier,
    propertyId: Long = -1L,
    addEditViewModel: AddEditViewModel,
    onCreatedClicked:(Long) -> Unit,
    onBackPressed:() -> Unit
) {
    if (propertyId > 0) addEditViewModel.getPropertyById(propertyId, currencyViewModel.currentCurrency)

    AddEditView(
        currencyViewModel = currencyViewModel,
        modifier = modifier,
        propertyId = propertyId,
        isAdd = propertyId == -1L,
        onCreatedClicked = onCreatedClicked,
        addEditViewModel = addEditViewModel
    )

    BackHandler {
        addEditViewModel.resetState()
        onBackPressed.invoke()
    }
}

/**
 * The composable that calls all the required composables to be ablke to add or edti a Property
 */
@Composable
private fun AddEditView(
    currencyViewModel: CurrencyViewModel,
    modifier: Modifier,
    propertyId: Long,
    onCreatedClicked:(Long) -> Unit,
    isAdd:Boolean,
    addEditViewModel: AddEditViewModel
){
    val context = LocalContext.current
    val state = addEditViewModel.state
    val scrollState = rememberScrollState()
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    val isAddOrUpdatePropertyFinished by rememberUpdatedState(addEditViewModel.isAddOrUpdatePropertyFinished)

    Column(
        modifier = modifier
            .padding(bottom = 0.dp)
            .fillMaxHeight()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        PriceAndAgentSection(currencyViewModel = currencyViewModel,addEditViewModel = addEditViewModel)

        MediaSection(addEditViewModel = addEditViewModel)

        DescriptionTypePickerSection(addEditViewModel = addEditViewModel)

        AddressSection(addEditViewModel = addEditViewModel, isPortrait = isPortrait)

        NearbyAmenities(
            isPortrait = isPortrait,
            nearbyPlaces = state.nearbyPlaces,
            onNearbyPlaceChanged = {
                addEditViewModel.onNearbyPlacesChanged(it)
            }
        )

        SoldDate(addEditViewModel = addEditViewModel)

        ValidationAndSaveButton(addEditViewModel = addEditViewModel, propertyId = propertyId)


        //used to listen to when the property has finished being added to the db or updated
        LaunchedEffect(isAddOrUpdatePropertyFinished) {
            if (isAddOrUpdatePropertyFinished) {
                if (!isAdd) {
                    Toast.makeText(context, "You have successfully updated a property!",Toast.LENGTH_SHORT).show()
                    onCreatedClicked.invoke(state.id)
                }
                else {
                    Toast.makeText(context, "You have successfully created a property!",Toast.LENGTH_SHORT).show()
                    onCreatedClicked.invoke(state.id)
                }
                addEditViewModel.resetFinishedState()
                addEditViewModel.resetState()
            }
        }
    }
}
/**
 * Composable for filling in the price and agent name of a property
 */
@Composable
fun PriceAndAgentSection(
    currencyViewModel: CurrencyViewModel,
    addEditViewModel: AddEditViewModel
) {
    currencyViewModel.getCurrency()
    val state = addEditViewModel.state
    Row(
        modifier = Modifier.fillMaxWidth(),
    ){
        OutlinedTextField(
            value = "${ state.price ?: "" }",
            onValueChange = {
                addEditViewModel.onPriceChange(it)
            },
            placeholder = { Text(text = "price") },
            modifier = Modifier
                .padding(8.dp)
                .weight(1f),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            leadingIcon = {
                Image(
                    painter = when(currencyViewModel.currentCurrency) {
                        CurrencyType.Dollar -> painterResource(id = R.drawable.dollar_image)
                        CurrencyType.Euro -> painterResource(id = R.drawable.euro_image)
                    },
                    contentDescription = "dollar"
                )
            },
            singleLine = true,
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
            },
            singleLine = true,
        )
    }
}

/**
 * Composable used for selecting the photos of a property
 */
@Composable
fun MediaSection(addEditViewModel: AddEditViewModel) {
    val state = addEditViewModel.state
    val context = LocalContext.current
    var isImageSelectChoiceVisible by remember { mutableStateOf(false) }
    //Used to selected on or multiple pictures
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

    //used for taking a picturte
    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ){
        if (it) {
            addEditViewModel.onImagesAdded(originalImagesUris = listOf(uri), context =  context)
        }
        else{
            Log.d("Add edit screen","failed to get uri")
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, launch the camera intent
            takePicture.launch(uri)
        } else {
            // Handle the case where permission is not granted
        }
    }

    Text(
        text = "Media",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.DarkGray,
        modifier = Modifier.padding(8.dp)
    )


    val addEditProperties = PropertyPhotosModel(id = -1L, photoPath = "", caption = "")//used for creating the button to add properties
    //combines the list of properties with the add property button
    val photosWithAddButton = if(!state.photos.isNullOrEmpty()) {
        state.photos + addEditProperties
    }else{
        mutableListOf(addEditProperties)
    }
    LazyRow(modifier = Modifier.padding(4.dp)) {
        itemsIndexed(photosWithAddButton) { index, photo ->
            if (photo.id != -1L){
                PhotoItem(
                    photo = photo,
                    onPhotoChanged = addEditViewModel::onPhotoCaptionChanged,
                    onRemovePhotoClicked = addEditViewModel::onImageRemoved,
                    index = index
                )
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
}

/**
 * Composable for selectin the trype of a property and filling out the description
 */
@Composable
fun DescriptionTypePickerSection(addEditViewModel: AddEditViewModel) {
    val state = addEditViewModel.state

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Text(
            text = "Description",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier
                .padding(8.dp)
        )

        PropertyTypePicker(
            onChangedTypePicker = { propertyType ->
                propertyType?.let {
                    addEditViewModel.onTypeChange(it)
                }
            },
            propertyType = state.type,
            hasNoTypeChoice = false
        )
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
}

/**
 * Composable used for entering the address details of a property
 * it takes into account what type of device is being used and orientation
 */
@Composable
fun AddressSection(addEditViewModel: AddEditViewModel, isPortrait:Boolean) {
    val state = addEditViewModel.state
    if (!isPortrait){
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Column(modifier = Modifier.weight(0.5f)){
                BaseDetails(
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
        BaseDetails(
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
}

/**
 * Composable used to change the sold satatus of a property and enter the date in which the property was sold
 */
@SuppressLint("SimpleDateFormat")
@Composable
fun SoldDate(addEditViewModel: AddEditViewModel) {
    val state = addEditViewModel.state
    val dateFormat = SimpleDateFormat("dd/MM/yy")
    val openDialog = remember { mutableStateOf(false) }
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
                            color = if(state.soldDate == null )MaterialTheme.colorScheme.error else Color.Gray,
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
}

/**
 * Composable used for the save button. It makes sure the form is valid before enabling the button
 */
@Composable
fun ValidationAndSaveButton(
    addEditViewModel: AddEditViewModel,
    propertyId: Long
) {
    Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
        if (!addEditViewModel.isFormValid){
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
            enabled = addEditViewModel.isFormValid
        ) {
            Text(text = if (propertyId > 0) "Save Changes" else "Create")
        }
    }
}

/**
 * Creates a new image file in the external cache directory of the application, using a timestamp
 * to generate a unique file name. The file is in JPEG format with a ".jpg" extension.
 *
 * @return A [File] object representing the newly created image file.
 */
@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"

    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}

