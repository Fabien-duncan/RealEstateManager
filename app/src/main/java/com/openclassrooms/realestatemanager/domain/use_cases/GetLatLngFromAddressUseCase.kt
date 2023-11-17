package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.domain.geocoding.GeocodingRepository
import com.openclassrooms.realestatemanager.domain.geocoding.LatLongEntity
import com.openclassrooms.realestatemanager.domain.repository.Respository
import javax.inject.Inject

class GetLatLngFromAddressUseCase @Inject constructor(
    private val geocodingRepository: GeocodingRepository
) {
    suspend operator fun invoke(address: String, apiKey:String): LatLongEntity? {
        return geocodingRepository.getLatLngFromAddress(address,apiKey)
    }
}