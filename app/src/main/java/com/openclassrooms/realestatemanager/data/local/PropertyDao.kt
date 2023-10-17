package com.openclassrooms.realestatemanager.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.data.local.model.PropertyAddress
import com.openclassrooms.realestatemanager.data.local.model.PropertyPhotos
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.data.local.model.PropertyNearbyPlaces
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import com.openclassrooms.realestatemanager.enums.PropertyType
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface PropertyDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insert(property: Property): Long

    @Update(onConflict =  OnConflictStrategy.REPLACE)
    fun update(property: Property)

    @Query("SELECT * FROM Property WHERE id = :propertyId")
    fun getPropertyById(propertyId: Long): Flow<Property>

    @Query("SELECT * FROM Property WHERE isSold = 0 ORDER BY createdDate")
    fun getAvailableProperties(): Flow<List<Property>>

    @Query("SELECT * FROM Property WHERE isSold = 1 ORDER BY createdDate")
    fun getSoldProperties(): Flow<List<Property>>

    @Query("SELECT * FROM Property ORDER BY createdDate")
    fun getAllProperties(): Flow<List<Property>>

    @Query("SELECT * FROM PropertyPhotos WHERE propertyId = :propertyId")
    fun getPropertyPhotos(propertyId: Long):Flow<List<PropertyPhotos>>

    @Query("SELECT * FROM PropertyAddress WHERE id = :addressId")
    fun getPropertyAddress(addressId: Long):Flow<PropertyAddress>

    @Query("SELECT * FROM PropertyNearbyPlaces WHERE propertyId = :propertyId")
    fun getPropertyNearbyPlaces(propertyId: Long):Flow<List<PropertyNearbyPlaces>>

    @Query("SELECT Property.*, COUNT(PropertyPhotos.propertyId) AS numPhotos FROM Property " +
            "LEFT JOIN PropertyPhotos ON Property.id = PropertyPhotos.propertyId " +
            "LEFT JOIN PropertyNearbyPlaces ON Property.id = PropertyNearbyPlaces.propertyID " +
            "WHERE " +
            "(:propertyType IS NULL OR type = :propertyType) " +
            "AND (:minPrice IS NULL OR price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR price <= :maxPrice) " +
            "AND (:minSurfaceArea IS NULL OR surfaceArea >= :minSurfaceArea) " +
            "AND (:maxSurfaceArea IS NULL OR surfaceArea <= :maxSurfaceArea) " +
            "AND (:minNumRooms IS NULL OR numRooms >= :minNumRooms) " +
            "AND (:maxNumRooms IS NULL OR numRooms <= :maxNumRooms) " +
            "AND (:minCreationDate IS NULL OR createdDate >= :minCreationDate) " +
            "AND (:maxCreationDate IS NULL OR createdDate <= :maxCreationDate) " +
            "AND (:isSold IS NULL OR isSold = :isSold) " +
            "AND (:minSoldDate IS NULL OR createdDate >= :minCreationDate) " +
            "AND (:maxSoldDate IS NULL OR soldData <= :maxSoldDate) " +
            "GROUP BY Property.id " +
            "HAVING (:minNumPictures IS NULL OR numPhotos >= :minNumPictures) " +
            "AND (:nearbyPlaceTypes IS NULL OR PropertyNearbyPlaces.placeName IN (:nearbyPlaceTypes)) " +
            "ORDER BY createdDate"
            )
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