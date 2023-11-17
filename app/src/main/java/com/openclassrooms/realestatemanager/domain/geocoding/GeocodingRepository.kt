package com.openclassrooms.realestatemanager.domain.geocoding

interface GeocodingRepository {
    suspend fun getLatLngFromAddress(address: String,apiKey: String): LatLongEntity?
}