package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.repository.Repository
import javax.inject.Inject

class AddPropertyUseCase @Inject constructor(
    private val repository: Repository
){
    suspend operator fun invoke(property: PropertyModel) = repository.insert(property)
}