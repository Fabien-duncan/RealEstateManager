package com.openclassrooms.realestatemanager.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.common.utils.DateUtils
import com.openclassrooms.realestatemanager.common.utils.TextUtils

/**
 * Composable that deals with extra details such as the nearby places, agent name and creation and sold date (if sold)
 */
@Composable
fun ExtraDetails(
    state: DetailState,
    isLargeView:Boolean,
    isPortrait: Boolean = false
) {
    val numberOfColumns = if (isLargeView && !isPortrait) 5 else 3

    Text(
        text = "Nearby Amenities",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.padding(8.dp)
    )

    if (state.property?.nearbyPlaces != null) {
        LazyVerticalGrid(
            modifier = Modifier
                .heightIn(min = 50.dp, max = 250.dp)
                .padding(8.dp),
            columns = GridCells.Fixed(numberOfColumns),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(state.property.nearbyPlaces.size) { index ->
                NearbyPlacesCells(TextUtils.capitaliseFirstLetter(state.property.nearbyPlaces[index].name))
            }
        }
    }else{
        Text(
            text = "There are no Amenities added to this property yet!!",
            color = MaterialTheme.colorScheme.error,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center
        )
    }

    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.primary)) {
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.agent_24),
                    contentDescription = "Name of Agent"
                )
                state.property?.let { Text(text = it.agentName) }
            }
            Row {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Name of Agent")
                if (isLargeView && !isPortrait) {
                    Text(text = " Creation: ", fontWeight = FontWeight.Bold)
                    state.property?.let { Text(text = DateUtils.formatDate(it.createdDate)) }
                    state.property?.soldDate?.let {
                        Text(text = " Sold: ", fontWeight = FontWeight.Bold)
                        Text(text = DateUtils.formatDate(it) )
                    }
                }else{
                    Column {
                        Row {
                            Text(text = " Creation: ", fontWeight = FontWeight.Bold)
                            state.property?.let { Text(text = DateUtils.formatDate(it.createdDate)) }
                        }
                        Row {
                            state.property?.soldDate?.let {
                                Text(text = " Sold: ", fontWeight = FontWeight.Bold)
                                Text(text = DateUtils.formatDate(it) )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun NearbyPlacesCells(text: String) {
    Text(text = text)
}