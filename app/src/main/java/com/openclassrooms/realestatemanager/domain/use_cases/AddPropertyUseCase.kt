package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.repository.Respository
import javax.inject.Inject

class AddPropertyUseCase @Inject constructor(
    private val repository: Respository
){
    suspend operator fun invoke(property: PropertyModel) = repository.insert(property)
}