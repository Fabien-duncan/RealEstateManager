package com.openclassrooms.realestatemanager.domain.model

import androidx.room.ColumnInfo
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType

data class PropertyNearbyPlacesModel(
    val id: Long = 0,
    val propertyId:Long,
    val nearbyType: NearbyPlacesType
)
