package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.data.local.model.PropertyAddress
import com.openclassrooms.realestatemanager.domain.repository.Respository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPropertyAddressUseCase @Inject constructor(
    private val repository: Respository
) {
    operator fun invoke(propertyId: Long):Flow<PropertyAddress> = repository.getPropertyAddress(propertyId)
}