package com.openclassrooms.realestatemanager.domain.repository

import android.location.Location
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.data.local.model.Address
import com.openclassrooms.realestatemanager.data.local.model.PropertyNearbyPlaces
import com.openclassrooms.realestatemanager.data.local.model.PropertyPhotos
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import com.openclassrooms.realestatemanager.enums.PropertyType
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface Repository {
    suspend fun insert(property: PropertyModel):Long
    suspend fun update(property: Property)
    fun getPropertyWithDetailsById(propertyId: Long): Flow<PropertyModel>
    fun getAvailableProperties(): Flow<List<Property>>
    fun getSoldProperties(): Flow<List<Property>>
    fun getAllProperties(): Flow<List<PropertyModel>>
    fun getPropertyPhotos(propertyId: Long):Flow<List<PropertyPhotos>>
    fun getPropertyAddress(addressId: Long):Flow<Address>
    fun getPropertyNearbyPlaces(propertyId: Long):Flow<List<PropertyNearbyPlaces>>
    suspend fun getCurrentLocation():Result<Location>
    fun getFilteredProperties(
        propertyType: PropertyType?=null,
        minPrice: Double?=null,
        maxPrice: Double?=null,
        minSurfaceArea: Int?=null,
        maxSurfaceArea: Int?=null,
        minNumRooms: Int?=null,
        maxNumRooms: Int?=null,
        minCreationDate: Date?=null,
        maxCreationDate: Date?=null,
        isSold: Boolean?=null,
        minSoldDate: Date?=null,
        maxSoldDate: Date?=null,
        minNumPictures: Int? = null,
        nearbyPlaceTypes: List<NearbyPlacesType>? = null,
    ):Flow<List<Property>>
}