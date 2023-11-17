package com.openclassrooms.realestatemanager.data.geocoding

import com.openclassrooms.realestatemanager.data.api.GeocodingApiService
import com.openclassrooms.realestatemanager.domain.geocoding.GeocodingRepository
import com.openclassrooms.realestatemanager.domain.geocoding.LatLongEntity
import javax.inject.Inject

class GeocodingRepositoryImp @Inject constructor(
    private val geocodingApiService : GeocodingApiService
): GeocodingRepository {
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