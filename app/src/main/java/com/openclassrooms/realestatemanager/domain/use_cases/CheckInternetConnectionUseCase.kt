package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.domain.Connection.ConnectionCheckerRepository
import javax.inject.Inject

class CheckInternetConnectionUseCase @Inject constructor(
    private val connectionCheckerRepository: ConnectionCheckerRepository
){
    operator fun invoke(): Boolean = connectionCheckerRepository.isInternetConnected()
}