package com.openclassrooms.realestatemanager.presentation.create_edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.R

/**
 * Composable that deals with the base details of a property, such :
 * surface, rooms, bedrooms, bathrooms.
 * It will addapt to the size of the screen: Large/small
 */
@Composable
fun BaseDetails(
    modifier: Modifier = Modifier,
    onAreaChanged: (String?) -> Unit,
    onRoomsChanged: (String?) -> Unit,
    onBedroomsChanged: (String?) -> Unit,
    onBathroomsChanged: (String?) -> Unit,
    state: AddEditState,
    isLargeView:Boolean
){

    if(isLargeView){
        Column {
            BaseDetailCard(
                painter = painterResource(id = R.drawable.area_image),
                title = "surface in m²",
                modifier = modifier,
                onValueChanged = onAreaChanged,
                value = state.rooms
            )
            BaseDetailCard(
                painter = painterResource(id = R.drawable.number_rooms_image),
                title = "No. rooms",
                modifier = modifier,
                onValueChanged = onRoomsChanged,
                value = state.rooms
            )
            BaseDetailCard(
                painter = painterResource(id = R.drawable.number_bathrooms_image),
                title = "No. bathrooms",
                modifier = modifier,
                onValueChanged = onBathroomsChanged,
                value = state.bathrooms
            )
            BaseDetailCard(
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
            val cardModifier = modifier
                .weight(1f)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {

                BaseDetailCard(
                    painter = painterResource(id = R.drawable.area_image),
                    title = "surface in m²",
                    modifier = cardModifier,
                    onValueChanged = onAreaChanged,
                    value = state.area

                )
                BaseDetailCard(
                    painter = painterResource(id = R.drawable.number_bathrooms_image),
                    title = "No. bathrooms",
                    modifier = cardModifier,
                    onValueChanged = onBathroomsChanged,
                    value = state.bathrooms
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                BaseDetailCard(
                    painter = painterResource(id = R.drawable.number_rooms_image),
                    title = "No. rooms",
                    modifier = cardModifier,
                    onValueChanged = onRoomsChanged,
                    value = state.rooms
                )
                BaseDetailCard(
                    painter = painterResource(id = R.drawable.number_bedrooms_image),
                    title = "No. bedrooms",
                    modifier = cardModifier,
                    onValueChanged = onBedroomsChanged,
                    value = state.bedrooms
                )
            }
        }
    }
}

/**
 * Composable that deals with the layout of each base detail
 */
@Composable
private fun BaseDetailCard(
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