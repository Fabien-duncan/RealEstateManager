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
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.common.utils.TextUtils
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType

@Composable
fun NearbyAmenities(
    isLargeView:Boolean,
    isPortrait: Boolean = false,
    addEditState: AddEditState,
    onNearbyPlaceChanged: (NearbyPlacesType) -> Unit,
){
    val numberOfColumns = if (!isPortrait) 5 else 2
    LazyVerticalGrid(
        modifier = Modifier
            .heightIn(min = 50.dp, max = 250.dp)
            .padding(8.dp),
        columns = GridCells.Fixed(numberOfColumns),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(NearbyPlacesType.values()){ nearbyType ->
            NearbyCheckBox(nearbyPlacesType = nearbyType, addEditState = addEditState, onNearbyPlaceChanged = onNearbyPlaceChanged)
        }
    }
}
@Composable
private fun NearbyCheckBox(
    nearbyPlacesType: NearbyPlacesType,
    addEditState: AddEditState,
    onNearbyPlaceChanged: (NearbyPlacesType) -> Unit,
){
    val isTicked = if (addEditState.nearbyPlaces != null) addEditState.nearbyPlaces.contains(nearbyPlacesType) else false

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