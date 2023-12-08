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

class DetailViewModel @AssistedInject constructor(
    private val getPropertyByIdUseCase: GetPropertyByIdUseCase,
    private val checkInternetConnectionUseCase: CheckInternetConnectionUseCase,
    @Assisted private val propertyId: Long
):ViewModel() {
    var state by mutableStateOf(DetailState(null))
        private set
    var mapImageLink by mutableStateOf("")
        private set
    init {
        getPropertyById()
    }
    private fun getPropertyById() = viewModelScope.launch {
        getPropertyByIdUseCase(propertyId).collectLatest {property ->
            state = state.copy(property = property)
            mapImageLink = if(checkInternetConnectionUseCase.invoke())getMapImage(property.address.latitude, property.address.longitude) else "no internet"
        }
    }
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

data class DetailState(
    val property: PropertyModel? = null
)
@Suppress("UNCHECKED_CAST")
class DetailedViewModelFactory(
    private val propertyId: Long,
    private val assistedFactory: DetailAssistedFactory,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return assistedFactory.create(propertyId) as T
    }
}

@AssistedFactory
interface DetailAssistedFactory {
    fun create(propertyId: Long): DetailViewModel
}