package com.openclassrooms.realestatemanager.data.local

import androidx.room.Database
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.data.local.converters.NearbyPlacesTypeConverter
import com.openclassrooms.realestatemanager.data.local.converters.PropertyTypeConverter
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.data.local.model.PropertyAddress
import com.openclassrooms.realestatemanager.data.local.model.PropertyNearbyPlaces
import com.openclassrooms.realestatemanager.data.local.model.PropertyPhotos

@TypeConverters(NearbyPlacesTypeConverter::class, PropertyTypeConverter::class)
@Database(
    entities = [Property::class, PropertyAddress::class, PropertyNearbyPlaces::class, PropertyPhotos::class],
    version = 1,
    exportSchema = false
)
abstract class RealEstateDataBase() {
    abstract val propertyDao: PropertyDao
}