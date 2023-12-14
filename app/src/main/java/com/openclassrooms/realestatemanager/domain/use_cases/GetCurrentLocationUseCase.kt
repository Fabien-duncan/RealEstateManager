package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.domain.location.LocationTracker
import javax.inject.Inject

class GetCurrentLocationUseCase@Inject constructor(
    private val locationTracker: LocationTracker
) {
    suspend operator fun invoke() = locationTracker.getCurrentLocation()
}