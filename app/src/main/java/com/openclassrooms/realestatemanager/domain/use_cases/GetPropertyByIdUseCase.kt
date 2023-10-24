package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.repository.Respository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPropertyByIdUseCase @Inject constructor(
    private val repository: Respository
) {
    operator fun invoke(id:Long): Flow<PropertyModel> = repository.getPropertyWithDetailsById(id)
}