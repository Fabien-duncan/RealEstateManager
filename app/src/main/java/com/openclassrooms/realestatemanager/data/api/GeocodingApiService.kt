package com.openclassrooms.realestatemanager.data.api

import com.openclassrooms.realestatemanager.data.geocoding.response.GeocodingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApiService {
    @GET("geocode/json")
    suspend fun getLatLngFromAddress(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): GeocodingResponse
}