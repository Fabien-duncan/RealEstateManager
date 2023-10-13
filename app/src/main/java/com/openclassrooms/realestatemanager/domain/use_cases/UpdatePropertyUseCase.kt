package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.domain.repository.Respository
import javax.inject.Inject

class UpdatePropertyUseCase @Inject constructor(
    private val repository: Respository
) {
  suspend operator fun invoke(property: Property) = repository.update(property)
}