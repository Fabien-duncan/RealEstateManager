package com.openclassrooms.realestatemanager.data.local.converters

import androidx.room.TypeConverter
import com.openclassrooms.realestatemanager.enums.PropertyType

class PropertyTypeConverter {
    @TypeConverter
    fun fromPropertyType(value: PropertyType): String {
        return value.name
    }

    @TypeConverter
    fun toPropertyType(value: String): PropertyType {
        return PropertyType.valueOf(value)
    }
}