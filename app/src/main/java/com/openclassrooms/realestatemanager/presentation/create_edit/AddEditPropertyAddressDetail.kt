package com.openclassrooms.realestatemanager.presentation.create_edit

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.openclassrooms.realestatemanager.presentation.navigation.CheckConnectionViewModel

/**
 * Composable to fill out the address details of a property
 */
@Composable
fun AddressDetail(
    state: AddEditState,
    onCheckAddressClicked: () -> Unit,
    onNumberChanged: (String?) -> Unit,
    onStreetChanged: (String) -> Unit,
    onExtraChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onStateChanged: (String) -> Unit,
    onCountryChanged: (String) -> Unit,
    onPostCodeChanged: (String) -> Unit,
    ){
    val checkConnectionViewModel: CheckConnectionViewModel = viewModel()
    Row(
        modifier = Modifier
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {

        Column(modifier = Modifier.weight(1f)) {
            Column {
                OutlinedTextField(
                    value = ("${ state.number ?: "" }"),
                    onValueChange = {
                        onNumberChanged.invoke(it)
                    },
                    placeholder = { Text(text = "number") },
                    modifier = Modifier.padding(4.dp),
                    singleLine = true,
                    isError = state.number == null
                )
                OutlinedTextField(
                    value = state.extra ?: "",
                    onValueChange = {
                        onExtraChanged.invoke(it)
                    },
                    placeholder = { Text(text = "extra") },
                    modifier = Modifier.padding(4.dp),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = state.state ?: "",
                    onValueChange = {
                        onStateChanged.invoke(it)
                    },
                    placeholder = { Text(text = "state") },
                    modifier = Modifier.padding(4.dp),
                    singleLine = true,
                    isError = state.state == null,
                )
                OutlinedTextField(
                    value = state.postalCode ?: "",
                    onValueChange = {
                        onPostCodeChanged.invoke(it)
                    },
                    placeholder = { Text(text = "post code") },
                    modifier = Modifier.padding(4.dp),
                    singleLine = true,
                    isError = state.postalCode == null,
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            OutlinedTextField(
                value = state.street ?: "",
                onValueChange = {
                    onStreetChanged.invoke(it)
                },
                placeholder = { Text(text = "street") },
                modifier = Modifier.padding(4.dp),
                singleLine = true,
                isError = state.street == null,
            )
            OutlinedTextField(
                value = state.city ?: "",
                onValueChange = {
                    onCityChanged.invoke(it)
                },
                placeholder = { Text(text = "city") },
                modifier = Modifier.padding(4.dp),
                singleLine = true,
                isError = state.city == null,
            )
            OutlinedTextField(
                value = state.country ?: "",
                onValueChange = {
                    onCountryChanged.invoke(it)
                },
                placeholder = { Text(text = "country") },
                modifier = Modifier.padding(4.dp),
                singleLine = true,
                isError = state.country == null,
            )
        }
    }
    Button(
        onClick = { onCheckAddressClicked.invoke() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        enabled = checkConnectionViewModel.isInternetOn()
    ) {
        Text(text = "Check address")
        if (!checkConnectionViewModel.isInternetOn()){
            Text(
                text = "No internet, please turn it on.",
                color = MaterialTheme.colorScheme.error,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            )
        }
    }

}

/**
 * Composable that handles the display of the map image of a property if the address is found
 * The user can then chose to validate it if it is the correct location
 */
@Composable
fun AddressMapImage(
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