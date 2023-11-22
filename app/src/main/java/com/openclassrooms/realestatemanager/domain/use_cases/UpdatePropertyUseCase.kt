package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.domain.repository.Repository
import javax.inject.Inject

class UpdatePropertyUseCase @Inject constructor(
    private val repository: Repository
) {
  suspend operator fun invoke(property: Property) = repository.update(property)
}