package com.openclassrooms.realestatemanager.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType

@Entity(
    tableName = "property_nearby_places",
    indices = [Index(value = ["property_id", "nearby_type"], unique = true)]
)
data class PropertyNearbyPlaces(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "property_id", index = true)
    val propertyId:Long,
    @ColumnInfo(name = "nearby_type")
    val nearbyType: NearbyPlacesType
)
