package com.openclassrooms.realestatemanager.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.use_cases.CheckInternetConnectionUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetPropertyByIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ViewModel for displaying details of a property. Handles retrieval of property details
 * and provides the necessary data for the associated UI.
 *
 * @param getPropertyByIdUseCase Use case for retrieving property details by ID.
 * @param checkInternetConnectionUseCase Use case for checking internet connection status.
 * @param propertyId Identifier of the property to display details for.
 */
class DetailViewModel @AssistedInject constructor(
    private val getPropertyByIdUseCase: GetPropertyByIdUseCase,
    private val checkInternetConnectionUseCase: CheckInternetConnectionUseCase,
    @Assisted private val propertyId: Long
):ViewModel() {
    /**
     * Mutable state representing the current state of the Detail screen.
     */
    var state by mutableStateOf(DetailState(null))
        private set
    /**
     * Mutable state representing the URL of the map image associated with the property.
     */
    var mapImageLink by mutableStateOf("")
        private set
    /**
     * Initializes the ViewModel and triggers the retrieval of property details.
     */
    init {
        getPropertyById()
    }
    /**
     * Private function to retrieve property details by ID and update the ViewModel state.
     */
    private fun getPropertyById() = viewModelScope.launch {
        getPropertyByIdUseCase(propertyId).collectLatest {property ->
            state = state.copy(property = property)
            mapImageLink = if(checkInternetConnectionUseCase.invoke())getMapImage(property.address.latitude, property.address.longitude) else "no internet"
        }
    }
    /**
     * Function to update property details by a new ID. Launches a coroutine to retrieve and update the state.
     *
     * @param id New identifier of the property.
     */
    fun updatePropertyById(id:Long) = viewModelScope.launch {
        getPropertyByIdUseCase(id).collectLatest {property ->
            state = state.copy(property = property)
            mapImageLink = if(checkInternetConnectionUseCase.invoke())getMapImage(property.address.latitude, property.address.longitude) else "no internet"
        }
    }
}
private fun getMapImage(lat:Double?, long:Double?):String{
    val key = BuildConfig.GMP_key
    return if (lat != null && long != null){
        "https://maps.googleapis.com/maps/api/staticmap?center=$lat,%20$long&format=jpg&markers=%7C$lat,%20$long&zoom=19&size=1200x600&key=$key"
    }
    else{
        ""
    }
}

/**
 * Data class representing the state of the Detail screen, including the property details.
 *
 * @property property Details of the property to display.
 */
data class DetailState(
    val property: PropertyModel? = null
)
/**
 * Factory class for creating instances of [DetailViewModel].
 *
 * @param propertyId Identifier of the property for which details are requested.
 * @param assistedFactory Assisted factory for creating [DetailViewModel] instances.
 */
@Suppress("UNCHECKED_CAST")
class DetailedViewModelFactory(
    private val propertyId: Long,
    private val assistedFactory: DetailAssistedFactory,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return assistedFactory.create(propertyId) as T
    }
}
/**
 * Assisted Factory interface for creating [DetailViewModel] instances with property ID.
 */
@AssistedFactory
interface DetailAssistedFactory {
    fun create(propertyId: Long): DetailViewModel
}