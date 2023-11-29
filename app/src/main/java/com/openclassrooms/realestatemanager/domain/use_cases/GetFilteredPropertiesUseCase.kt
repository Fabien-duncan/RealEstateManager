package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
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
        agentName:String? = null,
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
        nearbyPlaceTypes: List<NearbyPlacesType>? = null,
    ):Flow<List<PropertyModel>> = repository.getFilteredProperties(
        agentName = agentName,
        propertyType = propertyType,
        minPrice = minPrice,
        maxPrice = maxPrice,
        minSurfaceArea = minSurfaceArea,
        maxSurfaceArea = maxSurfaceArea,
        minNumRooms = minNumRooms,
        maxNumRooms = maxNumRooms,
        minNumBathrooms = minNumBathrooms,
        maxNumBathrooms = maxNumBathrooms,
        minNumBedrooms = minNumBedrooms,
        maxNumBedrooms = maxNumBedrooms,
        minCreationDate = minCreationDate,
        maxCreationDate = maxCreationDate,
        isSold = isSold,
        minSoldDate = minSoldDate,
        maxSoldDate = maxSoldDate,
        minNumPictures = minNumPictures,
        nearbyPlaceTypes =
            if (nearbyPlaceTypes != null) {
                if (nearbyPlaceTypes.isNotEmpty())nearbyPlaceTypes
                else null
            }
            else null

    )
}