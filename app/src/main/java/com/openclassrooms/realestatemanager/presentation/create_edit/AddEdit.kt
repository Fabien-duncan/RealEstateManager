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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.common.utils.TextUtils
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel
import com.openclassrooms.realestatemanager.enums.PropertyType

@Composable
fun AddEditScreen(
    modifier: Modifier = Modifier,
    propertyId: Long = -1,
    isLargeView:Boolean,
    onBackPressed:() -> Unit
) {
    println("in Detail Screen and the property id is $propertyId")

    AddEditView(modifier = modifier, isLargeView = isLargeView)

    BackHandler {
        onBackPressed.invoke()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditView(
    modifier: Modifier,
    /*state: AddEditState,*/
    isLargeView:Boolean
){
    val scrollState = rememberScrollState()
    //var photos = state.photos
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    var type by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var isTypePickerExpanded by remember { mutableStateOf(false) }
    var onTypeSelected by remember { mutableStateOf(PropertyType.HOUSE)    }

    Column(
        modifier = modifier
            .padding(bottom = 0.dp)
            .fillMaxHeight()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

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
        DropdownMenu(
            expanded = isTypePickerExpanded,
            onDismissRequest = { isTypePickerExpanded = false }
        ) {
            PropertyType.values().forEach { type ->
                DropdownMenuItem(
                    onClick = {
                        onTypeSelected = type
                        isTypePickerExpanded = false
                    },
                    text = {Text(text = type.name)}

                )
            }
        }

        OutlinedTextField(
            value = TextFieldValue(onTypeSelected.name),
            onValueChange = {},
            enabled = false,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        println("clicked on type")
                        isTypePickerExpanded = true
                    }
                )
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
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
                    isLargeView = true
                )

                AddressDetail(
                    //address = it.address,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    isLargeView = true
                )
            }
        }
        else{
            HouseDetails(
                //state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                isLargeView = false
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
                isLargeView = false
            )
        }
        /*ExtraDetails(
            state = state,
            modifier = Modifier,
            isLargeView = isLargeView,
            isPortrait = isPortrait
        )*/
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
    isLargeView:Boolean
){

    if(isLargeView){
        Column(
        ) {
            HouseDetailCard(
                painter = painterResource(id = R.drawable.area_image),
                title = "surface in m²",
                modifier = modifier
            )
            HouseDetailCard(
                painter = painterResource(id = R.drawable.number_rooms_image),
                title = "No. rooms",
                modifier = modifier
            )
            HouseDetailCard(
                painter = painterResource(id = R.drawable.number_bathrooms_image),
                title = "No. bathrooms",
                modifier = modifier
            )
            HouseDetailCard(
                painter = painterResource(id = R.drawable.number_bedrooms_image),
                title = "No. bedrooms",
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
                    title = "surface in m²",
                    modifier = modifier
                )
                HouseDetailCard(
                    painter = painterResource(id = R.drawable.number_bathrooms_image),
                    title = "No. bathrooms",
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
                    modifier = modifier
                )
                HouseDetailCard(
                    painter = painterResource(id = R.drawable.number_bedrooms_image),
                    title = "No. bedrooms",
                    modifier = modifier
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
    modifier: Modifier
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
                    onValueChange = { value = it },
                    placeholder = { Text(text = title) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
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
    isLargeView:Boolean
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
                    onValueChange = {number = it },
                    //label = { Text(text = "type") },
                    placeholder = { Text(text = "number") },
                    modifier = Modifier.padding(4.dp)
                )
                OutlinedTextField(
                    value = extra,
                    onValueChange = {street = it },
                    //label = { Text(text = "type") },
                    placeholder = { Text(text = "extra") },
                    modifier = Modifier.padding(4.dp)
                )
                OutlinedTextField(
                    value = state,
                    onValueChange = {state = it },
                    //label = { Text(text = "type") },
                    placeholder = { Text(text = "state") },
                    modifier = Modifier.padding(4.dp)
                )
                OutlinedTextField(
                    value = postalCode,
                    onValueChange = {postalCode = it },
                    //label = { Text(text = "type") },
                    placeholder = { Text(text = "post code") },
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            OutlinedTextField(
                value = street,
                onValueChange = {street = it },
                //label = { Text(text = "type") },
                placeholder = { Text(text = "street") },
                modifier = Modifier.padding(4.dp)
            )
            OutlinedTextField(
                value = city,
                onValueChange = {city = it },
                //label = { Text(text = "type") },
                placeholder = { Text(text = "city") },
                modifier = Modifier.padding(4.dp)
            )
            OutlinedTextField(
                value = country,
                onValueChange = {country = it },
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
