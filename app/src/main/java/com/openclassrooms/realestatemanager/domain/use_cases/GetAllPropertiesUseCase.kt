package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPropertiesUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<List<PropertyModel>> = repository.getAllProperties()
}