package com.openclassrooms.realestatemanager.data.local.converters

import androidx.room.TypeConverter
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType


class NearbyPlacesTypeConverter {
    @TypeConverter
    fun fromNearbyPlacesType(value: NearbyPlacesType): String {
        return value.name
    }

    @TypeConverter
    fun toNearbyPlacesType(value: String): NearbyPlacesType {
        return NearbyPlacesType.valueOf(value)
    }
}