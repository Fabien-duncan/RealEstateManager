package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.domain.Connection.ConnectionCheckerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckInternetConnectionUseCase @Inject constructor(
    private val connectionCheckerRepository: ConnectionCheckerRepository
){
    operator fun invoke(): Boolean = connectionCheckerRepository.isInternetConnected()
}