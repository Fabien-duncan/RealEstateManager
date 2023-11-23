package com.openclassrooms.realestatemanager.data.repository

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import com.openclassrooms.realestatemanager.data.local.PropertyDao
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.data.local.model.Address
import com.openclassrooms.realestatemanager.data.local.model.PropertyNearbyPlaces
import com.openclassrooms.realestatemanager.data.local.model.PropertyPhotos
import com.openclassrooms.realestatemanager.domain.mapper.PropertyMapper
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.repository.Repository
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import com.openclassrooms.realestatemanager.enums.PropertyType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import java.util.Date
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PropertyRepositoryImpl @Inject constructor(
    private val propertyDao: PropertyDao,
    private val propertyMapper: PropertyMapper,
    private val fusedLocationClient: FusedLocationProviderClient
): Repository{
    override suspend fun insert(property: PropertyModel): Long {
        val propertyRoomEntity = propertyMapper.propertyModelToRoom(property)
        val propertyId = propertyDao.insert(propertyRoomEntity)

        val addressRoomEntity = propertyMapper.addressModelToRoom(property.address, propertyId)


        propertyDao.insert(addressRoomEntity)

        property.nearbyPlaces?.let {
            val nearbyPlace =propertyMapper.nearbyPlacesToRoomEntities(it, propertyId)
            propertyDao.insertNearbyPlaces(nearbyPlace)
        }

        property.photos?.let {
            val photos = propertyMapper.listOfPropertyPhotosModelToRoom(it, propertyId)
            propertyDao.insert(photos)
        }


        return propertyId
    }

    override suspend fun update(property: Property) {
        propertyDao.update(property)
    }

    override fun getPropertyWithDetailsById(propertyId: Long): Flow<PropertyModel> {
        return propertyDao.getPropertyWithDetailsById(propertyId).mapNotNull { propertyWithAllDetails ->
            propertyMapper.mapToDomainModel(propertyWithAllDetails) }
    }

    override fun getAvailableProperties(): Flow<List<Property>> {
        return propertyDao.getAvailableProperties()
    }

    override fun getSoldProperties(): Flow<List<Property>> {
        return propertyDao.getSoldProperties()
    }

    override fun getAllProperties(): Flow<List<PropertyModel>> {
        return propertyDao.getAllProperties().map {
            propertiesWithAllDetails -> propertiesWithAllDetails.mapNotNull { propertyWithAllDetails ->
                propertyMapper.mapToDomainModel(propertyWithAllDetails)
            }
        }
    }

    override fun getPropertyPhotos(propertyId: Long): Flow<List<PropertyPhotos>> {
        return propertyDao.getPropertyPhotos(propertyId)
    }

    override fun getPropertyAddress(addressId: Long): Flow<Address> {
        return propertyDao.getPropertyAddress(addressId)
    }

    override fun getPropertyNearbyPlaces(propertyId: Long): Flow<List<PropertyNearbyPlaces>> {
        return propertyDao.getPropertyNearbyPlaces(propertyId)
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Result<Location> {
        println("getting location")
        return try {
            println("before fused Location")
            val locationResult = fusedLocationClient.lastLocation.await()
            println("After fused Location, location result: ${locationResult == null}")
            locationResult?.let {
                println("location was retrieve, location: $it")
                Result.success(it)
            } ?: Result.failure(Exception("Location not available"))
        } catch (e: Exception) {
            println("failed to retrieve Location")
            Result.failure(e)
        }
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
    private suspend fun Task<Location>.await(): Location = suspendCoroutine { continuation ->
        addOnSuccessListener { location ->
            continuation.resume(location)
        }
        addOnFailureListener { exception ->
            continuation.resumeWithException(exception)
        }
    }

}