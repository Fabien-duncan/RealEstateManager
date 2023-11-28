package com.openclassrooms.realestatemanager.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
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
import com.openclassrooms.realestatemanager.enums.PropertyType
import com.openclassrooms.realestatemanager.presentation.create_edit.DatePicker
import com.openclassrooms.realestatemanager.presentation.property_type_picker.PropertyTypePicker
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun BottomSheetFilter(

){
    val currentCurrency = CurrencyType.Dollar
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Bottom) {
        Column(modifier = Modifier
            .weight(0.5F)) {
            Text(
                text = "Filter Options",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
            )
            PersonFilter()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 16.dp, start = 8.dp, end = 4.dp, bottom = 4.dp),

            ){
                Text(text = "Type: ")
                PropertyTypePicker(
                    onChangedTypePicker = { propertyType -> println("property type now is: $propertyType") },
                    propertyType = null
                )
            }

            MinMaxFilter(
                min = "Min Price",
                max = "Max Price",
                image = when (currentCurrency) {
                    CurrencyType.Dollar -> painterResource(id = R.drawable.dollar_image)
                    CurrencyType.Euro -> painterResource(id = R.drawable.euro_image)
                },
            )

            MinMaxFilter(
                min = "Min m²",
                max = "Max m²",
                image = painterResource(id = R.drawable.area_image),
            )

            MinMaxFilter(
                min = "Min Rooms",
                max = "Max Rooms",
                image = painterResource(id = R.drawable.number_rooms_image),
            )

            MinMaxFilter(
                min = "Min Bathrooms",
                max = "Max Bathroom",
                image = painterResource(id = R.drawable.number_bathrooms_image),
            )

            MinMaxFilter(
                min = "Min Bedrooms",
                max = "Max Bedroom",
                image = painterResource(id = R.drawable.number_bedrooms_image),
            )

            MinMaxFilter(
                min = "Min Photos",
                max = null,
                image = painterResource(id = R.drawable.photo_image)
            )
            MinMaxDatePicker(date = null, dateTitle = "Created Date")

            MinMaxDatePicker(date = null, dateTitle = "Sold Date")
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)){
            Button(onClick = { /*TODO*/ }, modifier = Modifier.weight(1F)) {
                Text(text = "Cancel")
            }
            Button(onClick = { /*TODO*/ },modifier = Modifier.weight(1F)) {
                Text(text = "Filter")
            }
        }
    }
}
@Composable
private fun PersonFilter(

){
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 8.dp, top = 0.dp)){
        Image(
            painter = painterResource(id = R.drawable.agent_24),
            contentDescription = "Agent",
        )
        OutlinedTextField(
            value = "",
            onValueChange = {

            },
            placeholder = { Text(text = "Agent name") },
            label = { Text(text = "Agent name") },
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1F),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            maxLines = 1,
        )
    }
}

@Composable
private fun MinMaxFilter(
    min:String,
    max:String?,
    image:Painter
){
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 8.dp)) {
        Image(
            painter = image,
            contentDescription = "dollar"
        )
        OutlinedTextField(
            value = "",
            onValueChange = {

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
                value = "",
                onValueChange = {

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
    date: Date?,
    dateTitle:String
){
    val dateFormat = SimpleDateFormat("dd/MM/yy")
    var openDialog = remember { mutableStateOf(false) }
    var tempMinDate = remember { mutableStateOf<Date?>(null) }
    var tempMaxDate = remember { mutableStateOf<Date?>(null) }
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
            println("tempDate is : $tempMinDate")
            Text(
                fontStyle = FontStyle.Normal,
                text = if (tempMinDate.value != null) dateFormat.format(tempMinDate.value) else "Select max date",
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
            println("tempDate is : $tempMaxDate")
            Text(
                fontStyle = FontStyle.Normal,
                text = if (tempMaxDate.value != null) dateFormat.format(tempMaxDate.value) else "Select min date",
                modifier = Modifier
                    .padding(start = 12.dp)

            )
        }
        DatePicker(
            openDialog = openDialog,
            onSoldDateChanged = { date -> if (isMin.value) tempMinDate.value = date  else  tempMaxDate.value = date },
        )
    }
}