package com.openclassrooms.realestatemanager.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.enums.CurrencyType
import com.openclassrooms.realestatemanager.presentation.create_edit.DatePicker
import com.openclassrooms.realestatemanager.presentation.create_edit.NearbyAmenities
import com.openclassrooms.realestatemanager.presentation.home.HomeViewModel
import com.openclassrooms.realestatemanager.presentation.property_type_picker.PropertyTypePicker
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun BottomSheetFilter(
    filterViewModel: FilterViewModel,
    homeViewModel: HomeViewModel
){
    val currentCurrency = CurrencyType.Dollar
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Bottom) {
        Column(
            modifier = Modifier
                .weight(0.5F)
                .verticalScroll(state = scrollState),
        ) {
            Text(
                text = "Filter Options",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
            )
            PersonFilter(filterViewModel = filterViewModel)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 16.dp, start = 8.dp, end = 4.dp, bottom = 4.dp),

            ){
                Text(text = "Type: ")
                PropertyTypePicker(
                    onChangedTypePicker = { propertyType -> filterViewModel.onTypeChange(propertyType)
                    },
                    propertyType = filterViewModel.state.propertyType,
                    hasNoTypeChoice = true
                )
            }

            MinMaxFilter(
                min = "Min Price",
                max = "Max Price",
                image = when (currentCurrency) {
                    CurrencyType.Dollar -> painterResource(id = R.drawable.dollar_image)
                    CurrencyType.Euro -> painterResource(id = R.drawable.euro_image)
                },
                minValue = filterViewModel.state.minPrice,
                maxValue = filterViewModel.state.maxPrice,
                onMinValueChanged = {value -> filterViewModel.onMinPriceChange(value)},
                onMaxValueChanged = {value -> filterViewModel.onMaxPriceChange(value)},
            )

            MinMaxFilter(
                min = "Min m²",
                max = "Max m²",
                image = painterResource(id = R.drawable.area_image),
                minValue = filterViewModel.state.minSurface,
                maxValue = filterViewModel.state.maxSurface,
                onMinValueChanged = {value -> filterViewModel.onMinSurfaceChange(value)},
                onMaxValueChanged = {value -> filterViewModel.onMaxSurfaceChange(value)},
            )

            MinMaxFilter(
                min = "Min Rooms",
                max = "Max Rooms",
                image = painterResource(id = R.drawable.number_rooms_image),
                minValue = filterViewModel.state.minRooms,
                maxValue = filterViewModel.state.maxRooms,
                onMinValueChanged = {value -> filterViewModel.onMinRoomsChange(value)},
                onMaxValueChanged = {value -> filterViewModel.onMaxRoomsChange(value)},
            )

            MinMaxFilter(
                min = "Min Bathrooms",
                max = "Max Bathroom",
                image = painterResource(id = R.drawable.number_bathrooms_image),
                minValue = filterViewModel.state.minBathrooms,
                maxValue = filterViewModel.state.maxBathrooms,
                onMinValueChanged = {value -> filterViewModel.onMinBathroomsChange(value)},
                onMaxValueChanged = {value -> filterViewModel.onMaxBathroomsChange(value)},
            )

            MinMaxFilter(
                min = "Min Bedrooms",
                max = "Max Bedroom",
                image = painterResource(id = R.drawable.number_bedrooms_image),
                minValue = filterViewModel.state.minBedrooms,
                maxValue = filterViewModel.state.maxBedrooms,
                onMinValueChanged = {value -> filterViewModel.onMinBedroomsChange(value)},
                onMaxValueChanged = {value -> filterViewModel.onMaxBedroomsChange(value)},
            )

            MinMaxFilter(
                min = "Min Photos",
                max = null,
                image = painterResource(id = R.drawable.photo_image),
                minValue = filterViewModel.state.minPictures,
                maxValue = null,
                onMinValueChanged = {value -> filterViewModel.onMinPhotosChange(value)},
                onMaxValueChanged = {},
            )
            MinMaxDatePicker(
                mindate = filterViewModel.state.minCreationDate,
                maxdate = filterViewModel.state.maxCreationDate,
                onMinDateChanged = {filterViewModel.onMinCreatedDateChanged(it)},
                onMaxDateChanged = {filterViewModel.onMaxCreatedDateChanged(it)},
                dateTitle = "Created Date",
                onClearClicked = {
                    filterViewModel.onMinCreatedDateChanged(null)
                    filterViewModel.onMaxCreatedDateChanged(null)
                }
            )

            MinMaxDatePicker(
                mindate = filterViewModel.state.minSoldDate,
                maxdate = filterViewModel.state.maxSoldDate,
                onMinDateChanged = {filterViewModel.onMinSoldDateChanged(it)},
                onMaxDateChanged = {filterViewModel.onMaxSoldDateChanged(it)},
                dateTitle = "Sold Date",
                onClearClicked = {
                    filterViewModel.onMinSoldDateChanged(null)
                    filterViewModel.onMaxSoldDateChanged(null)
                }
            )

            //var nearbyPlaces = remember { mutableStateOf<List<NearbyPlacesType>>(mutableListOf()) }
            NearbyAmenities(
                isPortrait = true, nearbyPlaces = filterViewModel.state.nearbyPlaces,
                onNearbyPlaceChanged = {
                    filterViewModel.onNearbyPlacesChanged(it)
                },
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(bottom = 8.dp)
        ){
            Button(onClick = {

            }, modifier = Modifier
                .weight(1F)
                .padding(8.dp)) {
                Text(text = "Cancel")
            }
            Button(onClick = { homeViewModel.getFilteredProperties(filterViewModel.state)},modifier = Modifier
                .weight(1F)
                .padding(8.dp)) {
                Text(text = "Filter")
            }
            Button(onClick = { homeViewModel.getAllProperty()},modifier = Modifier
                .weight(1F)
                .padding(8.dp)) {
                Text(text = "Clear")
            }
        }
    }
}
@Composable
private fun PersonFilter(
    filterViewModel: FilterViewModel
){
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 8.dp, top = 0.dp)){
        Image(
            painter = painterResource(id = R.drawable.agent_24),
            contentDescription = "Agent",
        )
        OutlinedTextField(
            value = filterViewModel.state.agentName ?: "",
            onValueChange = {
                filterViewModel.onAgentChanged(it)
            },
            placeholder = { Text(text = "Agent name") },
            label = { Text(text = "Agent name") },
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1F),
            maxLines = 1,
        )
    }
}

@Composable
private fun MinMaxFilter(
    min:String,
    max:String?,
    image:Painter,
    minValue:Int?,
    maxValue:Int?,
    onMinValueChanged:(String?) -> Unit,
    onMaxValueChanged:(String?) -> Unit
){
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 8.dp)) {
        Image(
            painter = image,
            contentDescription = "dollar"
        )
        OutlinedTextField(
            value = "${ minValue ?: "" }",
            onValueChange = {
                onMinValueChanged.invoke(it)
            },
            placeholder = { Text(text = "Min") },
            label = { Text(text = min, maxLines = 1, overflow = TextOverflow.Ellipsis) },
            modifier = if(max != null ) Modifier
                .padding(4.dp)
                .weight(1F)else Modifier
                .padding(4.dp)
                .width(160.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
        if (max != null){
            OutlinedTextField(
                value = "${ maxValue ?: "" }",
                onValueChange = {
                    onMaxValueChanged.invoke(it)
                },
                placeholder = { Text(text = "Max ") },
                label = { Text(text = max, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1F),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            )
        }
    }
}
@Composable
private fun MinMaxDatePicker(
    mindate: Date?,
    maxdate: Date?,
    onMinDateChanged: (Date) -> Unit,
    onMaxDateChanged: (Date) -> Unit,
    onClearClicked:() -> Unit,
    dateTitle:String
){
    val dateFormat = SimpleDateFormat("dd/MM/yy")
    var openDialog = remember { mutableStateOf(false) }
    var isMin = remember { mutableStateOf(true)}

    Text(
        text = dateTitle,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color.DarkGray,
        modifier = Modifier.padding(8.dp)
    )

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(4.dp)){
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "date",
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(4.dp)
                .weight(1F)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .clickable {
                    openDialog.value = true
                    isMin.value = true
                },
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                fontStyle = FontStyle.Normal,
                text = if (mindate != null) dateFormat.format(mindate) else "Date min",
                modifier = Modifier
                    .padding(start = 12.dp)

            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(4.dp)
                .weight(1F)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .clickable {
                    openDialog.value = true
                    isMin.value = false
                },
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                fontStyle = FontStyle.Normal,
                text = if (maxdate != null) dateFormat.format(maxdate) else "Date max",
                modifier = Modifier
                    .padding(start = 12.dp)

            )
        }
        Button(onClick = { onClearClicked.invoke() }, modifier = Modifier.weight(0.8f).padding(4.dp)) {
            Text(text = "Clear")
        }
        DatePicker(
            openDialog = openDialog,
            onSoldDateChanged = { date -> if (isMin.value) onMinDateChanged.invoke(date)  else  onMaxDateChanged.invoke(date)},
        )
    }
}