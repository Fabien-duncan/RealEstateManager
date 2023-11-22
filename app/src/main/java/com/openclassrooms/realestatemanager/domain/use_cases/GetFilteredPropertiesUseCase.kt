package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.domain.repository.Repository
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import com.openclassrooms.realestatemanager.enums.PropertyType
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class GetFilteredPropertiesUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(
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
    ):Flow<List<Property>> = repository.getFilteredProperties(
        propertyType = propertyType,
        minPrice = minPrice,
        maxPrice = maxPrice,
        minSurfaceArea = minSurfaceArea,
        maxSurfaceArea = maxSurfaceArea,
        minNumRooms = minNumRooms,
        maxNumRooms = maxNumRooms,
        minCreationDate = minCreationDate,
        maxCreationDate = maxCreationDate,
        isSold = isSold,
        minSoldDate = minSoldDate,
        maxSoldDate = maxSoldDate,
        minNumPictures = minNumPictures,
        nearbyPlaceTypes = nearbyPlaceTypes
    )
}