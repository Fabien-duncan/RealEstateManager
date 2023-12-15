package com.openclassrooms.realestatemanager.presentation.navigation

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.domain.use_cases.CheckInternetConnectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel class for checking internet connection status.
 *
 * @property checkInternetConnectionUseCase Use case for checking internet connection.
 */
@HiltViewModel
class CheckConnectionViewModel @Inject constructor(
    private val checkInternetConnectionUseCase: CheckInternetConnectionUseCase
):ViewModel() {
    /**
     * Checks if the internet connection is available.
     *
     * @return `true` if the internet connection is available, `false` otherwise.
     */
    fun isInternetOn() = checkInternetConnectionUseCase.invoke()
}