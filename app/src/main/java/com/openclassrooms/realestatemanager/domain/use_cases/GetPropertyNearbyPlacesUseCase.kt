package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.data.local.model.PropertyNearbyPlaces
import com.openclassrooms.realestatemanager.domain.repository.Respository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPropertyNearbyPlacesUseCase @Inject constructor(
    private val repository: Respository
) {
    operator fun invoke(propertyId: Long):Flow<List<PropertyNearbyPlaces>> = repository.getPropertyNearbyPlaces(propertyId)
}