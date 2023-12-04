package com.openclassrooms.realestatemanager.presentation.home

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.Utils
import com.openclassrooms.realestatemanager.common.ScreenViewState
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.use_cases.GetAllPropertiesUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetCurrencyUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetCurrentLocationUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetFilteredPropertiesUseCase
import com.openclassrooms.realestatemanager.enums.CurrencyType
import com.openclassrooms.realestatemanager.presentation.navigation.FilterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllPropertiesUseCase: GetAllPropertiesUseCase,
    private val getFilteredPropertiesUseCase: GetFilteredPropertiesUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getCurrencyUseCase: GetCurrencyUseCase,
):ViewModel(){
    private val _state:MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val state:StateFlow<HomeState> = _state.asStateFlow()
    var isMapView by mutableStateOf(false)
    var currentLocation by mutableStateOf<Location?>(null)
    init {
        getAllProperty()
    }

    fun getAllProperty(){
        getAllPropertiesUseCase()
            .onEach {
                _state.value = HomeState(properties = ScreenViewState.Success(it))
            }
            .catch {
                _state.value = HomeState(properties = ScreenViewState.Error(it.message))
            }
            .launchIn(viewModelScope)
    }
    fun getCurrentLocation(){
        viewModelScope.launch {
            currentLocation = getCurrentLocationUseCase.invoke()
        }
    }
    fun getFilteredProperties(
        filterState: FilterState
    ){
        val minPrice: Int?
        val maxPrice: Int?
        when(getCurrencyUseCase.invoke()){
            CurrencyType.Euro -> {
                minPrice = if (filterState.minPrice == null) null else Utils.convertEuroToDollar(filterState.minPrice)
                maxPrice = if (filterState.maxPrice == null) null else Utils.convertEuroToDollar(filterState.maxPrice)
            }
            CurrencyType.Dollar -> {
                minPrice = filterState.minPrice
                maxPrice = filterState.maxPrice
            }
        }

        getFilteredPropertiesUseCase.invoke(
            agentName = filterState.agentName,
            propertyType = filterState.propertyType,
            minPrice = minPrice,
            maxPrice = maxPrice,
            minSurfaceArea = filterState.minSurface,
            maxSurfaceArea = filterState.maxSurface,
            minNumRooms = filterState.minRooms,
            maxNumRooms = filterState.maxRooms,
            minNumBathrooms = filterState.minBathrooms,
            maxNumBathrooms = filterState.maxBathrooms,
            minNumBedrooms = filterState.minBedrooms,
            maxNumBedrooms = filterState.maxBedrooms,
            minNumPictures = filterState.minPictures,
            minCreationDate = filterState.minCreationDate,
            maxCreationDate = filterState.maxCreationDate,
            isSold = filterState.isSold,
            minSoldDate = filterState.minSoldDate,
            maxSoldDate = filterState.maxSoldDate,
            nearbyPlaceTypes = filterState.nearbyPlaces
        )
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

