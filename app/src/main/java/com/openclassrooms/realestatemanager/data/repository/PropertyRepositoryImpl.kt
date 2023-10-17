package com.openclassrooms.realestatemanager.data.repository

import com.openclassrooms.realestatemanager.data.local.PropertyDao
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.data.local.model.PropertyAddress
import com.openclassrooms.realestatemanager.data.local.model.PropertyNearbyPlaces
import com.openclassrooms.realestatemanager.data.local.model.PropertyPhotos
import com.openclassrooms.realestatemanager.domain.repository.Respository
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import com.openclassrooms.realestatemanager.enums.PropertyType
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class PropertyRepositoryImpl @Inject constructor(
    private val propertyDao: PropertyDao
): Respository{
    override suspend fun insert(property: Property) {
        propertyDao.insert(property)
    }

    override suspend fun update(property: Property) {
        propertyDao.update(property)
    }

    override fun getPropertyById(propertyId: Long): Flow<Property> {
        return propertyDao.getPropertyById(propertyId)
    }

    override fun getAvailableProperties(): Flow<List<Property>> {
        return propertyDao.getAvailableProperties()
    }

    override fun getSoldProperties(): Flow<List<Property>> {
        return propertyDao.getSoldProperties()
    }

    override fun getAllProperties(): Flow<List<Property>> {
        return propertyDao.getAllProperties()
    }

    override fun getPropertyPhotos(propertyId: Long): Flow<List<PropertyPhotos>> {
        return propertyDao.getPropertyPhotos(propertyId)
    }

    override fun getPropertyAddress(addressId: Long): Flow<PropertyAddress> {
        return propertyDao.getPropertyAddress(addressId)
    }

    override fun getPropertyNearbyPlaces(propertyId: Long): Flow<List<PropertyNearbyPlaces>> {
        return propertyDao.getPropertyNearbyPlaces(propertyId)
    }

    override fun getFilteredProperties(
        propertyType: PropertyType?,
        minPrice: Double?,
        maxPrice: Double?,
        minSurfaceArea: Int?,
        maxSurfaceArea: Int?,
        minNumRooms: Int?,
        maxNumRooms: Int?,
        minCreationDate: Date?,
        maxCreationDate: Date?,
        isSold: Boolean?,
        minSoldDate: Date?,
        maxSoldDate: Date?,
        minNumPictures: Int?,
        nearbyPlaceTypes: List<NearbyPlacesType>?
    ): Flow<List<Property>> {
        return propertyDao.getFilteredProperties(
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

}