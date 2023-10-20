package com.openclassrooms.realestatemanager.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.common.ScreenViewState
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.data.local.model.PropertyWithAllDetails
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.use_cases.GetAllAvailablePropertiesUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetAllPropertiesUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetFilteredPropertiesUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetPropertyAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllAvailablePropertiesUseCase: GetAllAvailablePropertiesUseCase,
    private val getAllPropertiesUseCase: GetAllPropertiesUseCase,
    private val getPropertyAddressUseCase: GetPropertyAddressUseCase,
    private val getFilteredPropertiesUseCase: GetFilteredPropertiesUseCase
):ViewModel(){
    private val _state:MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val state:StateFlow<HomeState> = _state.asStateFlow()

    init {
        getAllProperty()
    }

    private fun getAllProperty(){
        getAllPropertiesUseCase()
            .onEach {
                _state.value = HomeState(properties = ScreenViewState.Success(it))
            }
            .catch {
                _state.value = HomeState(properties = ScreenViewState.Error(it.message))
            }
            .launchIn(viewModelScope)
    }
}

data class HomeState(
    val properties: ScreenViewState<List<PropertyModel>> = ScreenViewState.Loading,
)

