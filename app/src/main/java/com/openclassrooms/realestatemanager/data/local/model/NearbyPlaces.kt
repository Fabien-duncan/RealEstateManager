package com.openclassrooms.realestatemanager.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
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
    ]
)
data class NearbyPlaces(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val placeName: NearbyPlacesType
)
