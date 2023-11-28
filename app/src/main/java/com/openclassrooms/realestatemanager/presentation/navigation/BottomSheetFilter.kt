package com.openclassrooms.realestatemanager.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.enums.CurrencyType

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
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 8.dp)){
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
                .padding(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            maxLines = 1,
        )
    }
}

@Composable
private fun MinMaxFilter(
    min:String,
    max:String,
    image:Painter
){
    val currentCurrency = CurrencyType.Dollar
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
            modifier = Modifier
                .padding(8.dp)
                .weight(1F),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
        OutlinedTextField(
            value = "",
            onValueChange = {

            },
            placeholder = { Text(text = "Max ") },
            label = { Text(text = max, maxLines = 1, overflow = TextOverflow.Ellipsis) },
            modifier = Modifier
                .padding(8.dp)
                .weight(1F),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
    }
}