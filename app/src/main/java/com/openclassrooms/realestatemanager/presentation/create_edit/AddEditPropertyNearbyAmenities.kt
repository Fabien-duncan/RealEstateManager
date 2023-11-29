package com.openclassrooms.realestatemanager.presentation.create_edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openclassrooms.realestatemanager.common.utils.TextUtils
import com.openclassrooms.realestatemanager.data.local.model.PropertyNearbyPlaces
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType

@Composable
fun NearbyAmenities(
    isPortrait: Boolean = false,
    nearbyPlaces: List<NearbyPlacesType>?,
    onNearbyPlaceChanged: (NearbyPlacesType) -> Unit,
){
    Text(
        text = "Nearby Amenities",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.DarkGray,
        modifier = Modifier.padding(8.dp)
    )

    val numberOfColumns = if (!isPortrait) 5 else 2
    LazyVerticalGrid(
        modifier = Modifier
            .heightIn(min = 50.dp, max = 250.dp)
            .padding(8.dp),
        columns = GridCells.Fixed(numberOfColumns),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(NearbyPlacesType.values()){ nearbyType ->
            NearbyCheckBox(nearbyPlacesType = nearbyType, nearbyPlaces = nearbyPlaces, onNearbyPlaceChanged = onNearbyPlaceChanged)
        }
    }
}
@Composable
private fun NearbyCheckBox(
    nearbyPlacesType: NearbyPlacesType,
    nearbyPlaces: List<NearbyPlacesType>?,
    onNearbyPlaceChanged: (NearbyPlacesType) -> Unit,
){
    val isTicked = nearbyPlaces?.contains(nearbyPlacesType) ?: false

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