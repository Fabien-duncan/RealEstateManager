package com.openclassrooms.realestatemanager.presentation.navigation

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.domain.use_cases.CheckInternetConnectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckConnectionViewModel @Inject constructor(
    private val checkInternetConnectionUseCase: CheckInternetConnectionUseCase
):ViewModel() {
    fun isInternetOn() = checkInternetConnectionUseCase.invoke()
}