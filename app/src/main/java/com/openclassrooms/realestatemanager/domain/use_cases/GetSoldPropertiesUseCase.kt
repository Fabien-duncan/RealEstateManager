package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSoldPropertiesUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<List<Property>> = repository.getSoldProperties()
}