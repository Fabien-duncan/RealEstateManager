package com.openclassrooms.realestatemanager.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType

@Entity
data class PropertyNearbyPlaces(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "property_id", index = true)
    val propertyId:Long,
    val type: NearbyPlacesType
)
