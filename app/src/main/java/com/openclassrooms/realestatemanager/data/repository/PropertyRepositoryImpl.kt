package com.openclassrooms.realestatemanager.data.repository

import com.openclassrooms.realestatemanager.data.local.PropertyDao
import com.openclassrooms.realestatemanager.domain.mapper.AddressMapper
import com.openclassrooms.realestatemanager.domain.mapper.NearbyPlacesMapper
import com.openclassrooms.realestatemanager.domain.mapper.PropertyMapper
import com.openclassrooms.realestatemanager.domain.mapper.PropertyPhotosMapper
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.repository.Repository
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import com.openclassrooms.realestatemanager.enums.PropertyType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import java.util.Date
import javax.inject.Inject

/**
 * Implementation of [Repository] for managing property data using Room database.
 *
 * @property propertyDao Data Access Object for property entities.
 * @property propertyMapper Mapper for converting between property models and Room entities.
 * @property addressMapper Mapper for converting between address models and Room entities.
 * @property nearbyPlacesMapper Mapper for converting between nearby places models and Room entities.
 * @property propertyPhotosMapper Mapper for converting between property photos models and Room entities.
 */
class PropertyRepositoryImpl @Inject constructor(
    private val propertyDao: PropertyDao,
    private val propertyMapper: PropertyMapper,
    private val addressMapper: AddressMapper,
    private val nearbyPlacesMapper: NearbyPlacesMapper,
    private val propertyPhotosMapper: PropertyPhotosMapper
): Repository{
    /**
     * Inserts a new property into the database along with its associated address, nearby places, and photos.
     *
     * @param property The property model to insert.
     * @return The ID of the inserted property.
     */
    override suspend fun insert(property: PropertyModel): Long {
        val propertyRoomEntity = propertyMapper.mapToRoomEntity(property)
        val propertyId = propertyDao.insert(propertyRoomEntity)

        val addressRoomEntity = addressMapper.mapToRoomEntity(property.address, propertyId)

        propertyDao.insert(addressRoomEntity)

        property.nearbyPlaces?.let { nearbyPlacesType ->
            val nearbyPlace =nearbyPlacesMapper.mapToRoomEntities(nearbyPlacesType, propertyId)
            val nearbyPlacesIds = propertyDao.insertNearbyPlaces(nearbyPlace)
            propertyDao.clearNearbyPlacesForProperty(propertyId, nearbyPlacesIds)
        }

        property.photos?.let {
            val photos = propertyPhotosMapper.mapToRoomEntities(it, propertyId)
            val photosIds = propertyDao.insert(photos)
            propertyDao.clearPhotosForProperty(propertyId,photosIds)
        }

        return propertyId
    }

    /**
     * Retrieves a property with details by its ID as a [Flow].
     *
     * @param propertyId The ID of the property to retrieve.
     * @return A [Flow] emitting the [PropertyModel] with details for the given ID.
     */
    override fun getPropertyWithDetailsById(propertyId: Long): Flow<PropertyModel> {
        return propertyDao.getPropertyWithDetailsById(propertyId).mapNotNull { propertyWithAllDetails ->
            propertyMapper.mapToDomainModel(propertyWithAllDetails) }
    }

    /**
     * Retrieves all properties with details as a [Flow].
     *
     * @return A [Flow] emitting a list of [PropertyModel] with details.
     */
    override fun getAllPropertiesWithDetails(): Flow<List<PropertyModel>> {
        return propertyDao.getAllProperties().map {
            propertiesWithAllDetails -> propertiesWithAllDetails.map { propertyWithAllDetails ->
                propertyMapper.mapToDomainModel(propertyWithAllDetails)
            }
        }
    }

    /**
     * Retrieves filtered properties based on specified criteria as a [Flow].
     *
     * @param 'all' the parameters are the potential filters
     * @return A [Flow] emitting a list of [PropertyModel] with details based on the specified criteria.
     */
    override fun getFilteredProperties(
        agentName: String?,
        propertyType: PropertyType?,
        minPrice: Int?,
        maxPrice: Int?,
        minSurfaceArea: Int?,
        maxSurfaceArea: Int?,
        minNumRooms: Int?,
        maxNumRooms: Int?,
        minNumBathrooms: Int?,
        maxNumBathrooms: Int?,
        minNumBedrooms: Int?,
        maxNumBedrooms: Int?,
        minCreationDate: Date?,
        maxCreationDate: Date?,
        isSold: Boolean?,
        minSoldDate: Date?,
        maxSoldDate: Date?,
        minNumPictures: Int?,
        nearbyPlaceTypes: List<NearbyPlacesType>?
    ): Flow<List<PropertyModel>> {
        return if (nearbyPlaceTypes != null) {
            propertyDao.getFilterPropertiesWithNearByPlaces(
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
                nearbyPlaceTypes = nearbyPlaceTypes
            )
        }else{
            propertyDao.getFilterProperties(
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
            )
        }.map {
            propertiesWithAllDetails -> propertiesWithAllDetails.map { propertyWithAllDetails ->
                    propertyMapper.mapToDomainModel(propertyWithAllDetails)
            }
        }
    }
}