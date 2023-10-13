package com.openclassrooms.realestatemanager.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Property::class,
            parentColumns = ["id"],
            childColumns = ["propertyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["propertyId"])]
)
data class PropertyNearbyPlaces(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val propertyId:Long,
    val placeName: NearbyPlacesType
)
