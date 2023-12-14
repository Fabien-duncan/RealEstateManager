package com.openclassrooms.realestatemanager.data.geocoding

import com.openclassrooms.realestatemanager.data.api.GeocodingApiService
import com.openclassrooms.realestatemanager.domain.geocoding.GeocodingRepository
import com.openclassrooms.realestatemanager.domain.geocoding.LatLongEntity
import javax.inject.Inject

/**
 * Implementation of [GeocodingRepository] that interacts with the Geocoding API to retrieve
 * latitude and longitude information based on a given address.
 *
 * @param geocodingApiService The service responsible for making requests to the Geocoding API.
 */
class GeocodingRepositoryImp @Inject constructor(
    private val geocodingApiService : GeocodingApiService
): GeocodingRepository {
    /**
     * Retrieves latitude and longitude information from the Geocoding API based on the provided address.
     *
     * @param address The address for which latitude and longitude information is requested.
     * @param apiKey The API key required for accessing the Geocoding API.
     * @return [LatLongEntity] containing the latitude and longitude, or null if not found.
     */
    override suspend fun getLatLngFromAddress(address: String, apiKey: String): LatLongEntity? {
        return try {
            val response = geocodingApiService.getLatLngFromAddress(address, apiKey)
            if (response.results.isNotEmpty()) {
                val result = response.results[0]
                val location = result.geometry.location
                LatLongEntity(location.lat, location.lng)
            } else {
                null
            }
        } catch (e: Exception) {
            // Handle exceptions
            null
        }
    }
}