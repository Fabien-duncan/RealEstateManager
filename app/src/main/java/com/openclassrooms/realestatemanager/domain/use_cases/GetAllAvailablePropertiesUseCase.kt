package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.domain.repository.Respository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAvailablePropertiesUseCase @Inject constructor(
    private val repository: Respository
) {
    operator fun invoke(): Flow<List<Property>> = repository.getAvailableProperties()
}