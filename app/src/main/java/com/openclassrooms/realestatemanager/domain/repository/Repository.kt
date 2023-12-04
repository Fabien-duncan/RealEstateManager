package com.openclassrooms.realestatemanager.domain.repository

import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import com.openclassrooms.realestatemanager.enums.PropertyType
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface Repository {
    suspend fun insert(property: PropertyModel):Long
    fun getPropertyWithDetailsById(propertyId: Long): Flow<PropertyModel>
    fun getAllPropertiesWithDetails(): Flow<List<PropertyModel>>
    fun getFilteredProperties(
        agentName: String? = null,
        propertyType: PropertyType?=null,
        minPrice: Int?=null,
        maxPrice: Int?=null,
        minSurfaceArea: Int?=null,
        maxSurfaceArea: Int?=null,
        minNumRooms: Int?=null,
        maxNumRooms: Int?=null,
        minNumBathrooms: Int?=null,
        maxNumBathrooms: Int?=null,
        minNumBedrooms: Int?=null,
        maxNumBedrooms: Int?=null,
        minCreationDate: Date?=null,
        maxCreationDate: Date?=null,
        isSold: Boolean?=null,
        minSoldDate: Date?=null,
        maxSoldDate: Date?=null,
        minNumPictures: Int? = null,
        nearbyPlaceTypes: List<NearbyPlacesType>? = null
    ):Flow<List<PropertyModel>>
}