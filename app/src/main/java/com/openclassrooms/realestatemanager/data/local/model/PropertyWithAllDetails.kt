package com.openclassrooms.realestatemanager.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation

data class PropertyWithAllDetails(
    @Embedded
    val property: Property,
    @Relation(
        parentColumn = "id",
        entityColumn = "property_id"
    )
    val address: Address,
    @Relation(
        parentColumn = "id",
        entityColumn = "property_id"
    )
    val photos: List<PropertyPhotos>?,
    @Relation(
        parentColumn = "id",
        entityColumn = "property_id"
    )
    val nearbyPlaces: List<PropertyNearbyPlaces>?
)