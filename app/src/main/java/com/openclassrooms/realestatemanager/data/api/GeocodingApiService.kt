package com.openclassrooms.realestatemanager.data.api

import com.openclassrooms.realestatemanager.data.geocoding.response.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query
/**
 * Interface for interacting with a Geocoding API to convert addresses into latitude and longitude.
 *
 * @see GeocodingResponse
 */
interface GeocodingApiService {
    /**
     * Retrieves the latitude and longitude coordinates for a given address using the Geocoding API.
     *
     * @param address The address to be geocoded.
     * @param apiKey The API key for accessing the Geocoding service.
     * @return A [GeocodingResponse] containing the results of the geocoding operation.
     */
    @GET("geocode/json")
    suspend fun getLatLngFromAddress(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): GeocodingResponse
}