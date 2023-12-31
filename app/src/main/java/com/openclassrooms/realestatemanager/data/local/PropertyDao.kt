package com.openclassrooms.realestatemanager.data.local

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.openclassrooms.realestatemanager.data.local.model.Address
import com.openclassrooms.realestatemanager.data.local.model.PropertyPhotos
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.data.local.model.PropertyNearbyPlaces
import com.openclassrooms.realestatemanager.data.local.model.PropertyWithAllDetails
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import com.openclassrooms.realestatemanager.enums.PropertyType
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface PropertyDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insert(property: Property): Long

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insert(address: Address):Long
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insert(photos: List<PropertyPhotos>):List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNearbyPlaces(nearbyPlaces: List<PropertyNearbyPlaces>):List<Long>

    @Update(onConflict =  OnConflictStrategy.REPLACE)
    fun update(property: Property)

    @Query("DELETE FROM property_nearby_places WHERE property_id = :propertyId AND id NOT IN (:ids)")
    fun clearNearbyPlacesForProperty(propertyId:Long, ids:List<Long>)
    @Query("DELETE FROM property_photos WHERE property_id = :propertyId AND id NOT IN (:ids)")
    fun clearPhotosForProperty(propertyId:Long, ids:List<Long>)

    @Query("SELECT * FROM properties WHERE id = :propertyId")
    fun getPropertyById(propertyId: Long): Flow<Property>

    @Transaction
    @Query("SELECT * FROM properties WHERE id = :propertyId")
    fun getPropertyWithDetailsById(propertyId: Long): Flow<PropertyWithAllDetails>

    @Query("SELECT * FROM properties WHERE is_sold = 0 ORDER BY created_date")
    fun getAvailableProperties(): Flow<List<Property>>

    @Query("SELECT * FROM properties WHERE is_sold = 1 ORDER BY created_date")
    fun getSoldProperties(): Flow<List<Property>>

    @Transaction
    @Query("SELECT * FROM properties")
    fun getAllProperties(): Flow<List<PropertyWithAllDetails>>

    @Query("SELECT * FROM property_photos WHERE property_id = :propertyId")
    fun getPropertyPhotos(propertyId: Long):Flow<List<PropertyPhotos>>

    @Query("SELECT * FROM addresses WHERE id = :addressId")
    fun getPropertyAddress(addressId: Long):Flow<Address>

    @Query("SELECT * FROM property_nearby_places WHERE property_id = :propertyId")
    fun getPropertyNearbyPlaces(propertyId: Long):Flow<List<PropertyNearbyPlaces>>

    @Query("SELECT properties.*, COUNT(property_photos.property_id) AS numPhotos FROM properties " +
            "LEFT JOIN property_photos ON properties.id = property_photos.property_id " +
            "WHERE " +
            "(:propertyType IS NULL OR type = :propertyType) " +
            "AND (:agentName IS NULL OR agent_name LIKE '%' || :agentName || '%') " +
            "AND(:minPrice IS NULL OR price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR price <= :maxPrice)" +
            "AND (:minSurfaceArea IS NULL OR area >= :minSurfaceArea) " +
            "AND (:maxSurfaceArea IS NULL OR area <= :maxSurfaceArea) " +
            "AND (:minNumRooms IS NULL OR rooms >= :minNumRooms) " +
            "AND (:maxNumRooms IS NULL OR rooms <= :maxNumRooms) " +
            "AND (:minNumBathrooms IS NULL OR rooms >= :minNumBathrooms) " +
            "AND (:maxNumBathrooms IS NULL OR rooms <= :maxNumBathrooms) " +
            "AND (:minNumBedrooms IS NULL OR rooms >= :minNumBedrooms) " +
            "AND (:maxNumBedrooms IS NULL OR rooms <= :maxNumBedrooms) " +
            "AND (:minCreationDate IS NULL OR created_date >= :minCreationDate) " +
            "AND (:maxCreationDate IS NULL OR created_date <= :maxCreationDate) " +
            "AND (:isSold IS NULL OR is_sold = :isSold) " +
            "AND (:minSoldDate IS NULL OR created_date >= :minCreationDate) " +
            "AND (:maxSoldDate IS NULL OR sold_date <= :maxSoldDate) " +
            "GROUP BY properties.id " +
            "HAVING (:minNumPictures IS NULL OR numPhotos >= :minNumPictures) "
    )
    fun getFilterProperties(
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
    ): Flow<List<PropertyWithAllDetails>>
    @Query("SELECT properties.*, COUNT(property_photos.property_id) AS numPhotos FROM properties " +
            "LEFT JOIN property_photos ON properties.id = property_photos.property_id " +
            "LEFT JOIN property_nearby_places ON properties.id = property_nearby_places.property_id " +
            "WHERE " +
            "(:propertyType IS NULL OR type = :propertyType) " +
            "AND (:agentName IS NULL OR agent_name LIKE '%' || :agentName || '%') " +
            "AND(:minPrice IS NULL OR price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR price <= :maxPrice)" +
            "AND (:minSurfaceArea IS NULL OR area >= :minSurfaceArea) " +
            "AND (:maxSurfaceArea IS NULL OR area <= :maxSurfaceArea) " +
            "AND (:minNumRooms IS NULL OR rooms >= :minNumRooms) " +
            "AND (:maxNumRooms IS NULL OR rooms <= :maxNumRooms) " +
            "AND (:minNumBathrooms IS NULL OR rooms >= :minNumBathrooms) " +
            "AND (:maxNumBathrooms IS NULL OR rooms <= :maxNumBathrooms) " +
            "AND (:minNumBedrooms IS NULL OR rooms >= :minNumBedrooms) " +
            "AND (:maxNumBedrooms IS NULL OR rooms <= :maxNumBedrooms) " +
            "AND (:minCreationDate IS NULL OR created_date >= :minCreationDate) " +
            "AND (:maxCreationDate IS NULL OR created_date <= :maxCreationDate) " +
            "AND (:isSold IS NULL OR is_sold = :isSold) " +
            "AND (:minSoldDate IS NULL OR created_date >= :minCreationDate) " +
            "AND (:maxSoldDate IS NULL OR sold_date <= :maxSoldDate) " +
            "AND (property_nearby_places.nearby_type IN (:nearbyPlaceTypes)) " +
            "GROUP BY properties.id " +
            "HAVING (:minNumPictures IS NULL OR numPhotos >= :minNumPictures) "
    )
    fun getFilterPropertiesWithNearByPlaces(
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
    ): Flow<List<PropertyWithAllDetails>>

    @Query("SELECT * FROM properties")
    fun getAllPropertiesAsCursor(): Cursor
    @Query("SELECT * FROM properties WHERE id = :propertyId")
    fun getPropertyByIdAsCursor(propertyId: Long): Cursor
    @Query("SELECT * FROM addresses")
    fun getAllAddressesAsCursor(): Cursor
    @Query("SELECT * FROM property_nearby_places")
    fun getAllNearbyPlacesAsCursor(): Cursor
    @Query("SELECT * FROM property_photos")
    fun getAllPhotosAsCursor(): Cursor
}