package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.data.local.model.PropertyPhotos
import com.openclassrooms.realestatemanager.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPropertyPhotosUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(propertyId: Long):Flow<List<PropertyPhotos>> = repository.getPropertyPhotos(propertyId)
}