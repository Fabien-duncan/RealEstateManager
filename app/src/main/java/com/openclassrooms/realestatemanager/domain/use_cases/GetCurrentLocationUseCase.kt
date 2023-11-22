package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.domain.repository.Repository
import javax.inject.Inject

class GetCurrentLocationUseCase@Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke() = repository.getCurrentLocation()
}