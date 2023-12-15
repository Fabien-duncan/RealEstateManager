package com.openclassrooms.realestatemanager.presentation.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.R

/**
 * Composable for displaying the base details of a Property (surface area, number of rooms, bedrooms and bathrooms)
 * The display will adapt to the width of the screen by using the parameter isLargeView
 */
@Composable
fun BaseDetails(
    state: DetailState,
    modifier: Modifier = Modifier,
    isLargeView:Boolean
){

    if(isLargeView){
        Column( modifier = Modifier.padding(end = 20.dp)
        ) {
            BaseDetailCard(
                painter = painterResource(id = R.drawable.area_image),
                title = "surface",
                value = "${state.property?.area} m²",
                modifier = modifier
            )
            BaseDetailCard(
                painter = painterResource(id = R.drawable.number_rooms_image),
                title = "No. rooms",
                value = state.property?.rooms.toString(),
                modifier = modifier
            )
            BaseDetailCard(
                painter = painterResource(id = R.drawable.number_bathrooms_image),
                title = "No. bathrooms",
                value = state.property?.bathrooms.toString(),
                modifier = modifier
            )
            BaseDetailCard(
                painter = painterResource(id = R.drawable.number_bedrooms_image),
                title = "No. bedrooms",
                value = state.property?.bedrooms.toString(),
                modifier = modifier
            )
        }
    }
    else{
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val detailCardModifier = modifier
                .weight(1f)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {

                BaseDetailCard(
                    painter = painterResource(id = R.drawable.area_image),
                    title = "surface",
                    value = "${state.property?.area} m²",
                    modifier = detailCardModifier
                )
                BaseDetailCard(
                    painter = painterResource(id = R.drawable.number_bathrooms_image),
                    title = "No. bathrooms",
                    value = state.property?.bathrooms.toString(),
                    modifier = detailCardModifier
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                BaseDetailCard(
                    painter = painterResource(id = R.drawable.number_rooms_image),
                    title = "No. rooms",
                    value = state.property?.rooms.toString(),
                    modifier = detailCardModifier
                )
                BaseDetailCard(
                    painter = painterResource(id = R.drawable.number_bedrooms_image),
                    title = "No. bedrooms",
                    value = state.property?.bedrooms.toString(),
                    modifier = detailCardModifier
                )
            }
        }
    }
}

/**
 * Composable to handle the display of each detail
 */
@Composable
private fun BaseDetailCard(
    painter: Painter,
    title:String,
    value:String,
    modifier: Modifier
){
    Box(
        modifier = modifier,
    ) {
        Row {
            Image(painter = painter, contentDescription = title, modifier = Modifier.padding(vertical = 8.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = title)
                Text(text = value, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp))
            }

        }
    }
}