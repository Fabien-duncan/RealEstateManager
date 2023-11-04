package com.openclassrooms.realestatemanager.presentation.create_edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import com.openclassrooms.realestatemanager.domain.model.AddressModel
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel
import com.openclassrooms.realestatemanager.domain.use_cases.AddPropertyUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.UpdatePropertyUseCase
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import com.openclassrooms.realestatemanager.enums.PropertyType
import com.openclassrooms.realestatemanager.presentation.detail.DetailSate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
@HiltViewModel
class AddEditViewModel @Inject constructor(
    val addPropertyUseCase: AddPropertyUseCase,
    val updatePropertyUseCase: UpdatePropertyUseCase
):ViewModel() {
    var state by mutableStateOf(AddEditState())
        private set
    val isFormValid: Boolean
        get() = checkFormIsValid()

    private val property: PropertyModel
        get() = state.run {
            PropertyModel(
                price = 2000000.00,
                type = type!!,
                area = area!!,
                rooms = rooms!!,
                bedrooms = bedrooms!!,
                bathrooms = bathrooms!!,
                description = description!!,
                isSold = isSold,
                createdDate = Date(),
                soldDate = soldDate,
                agentName = "Fabien Duncan",
                address = AddressModel(
                    propertyId = -1L,
                    street = street!!,
                    extra = extra,
                    city = city!!,
                    state = state!!,
                    country = country!!,
                    postalCode = postalCode!!,
                    latitude = latitude,
                    longitude = longitude
                ),
                nearbyPlaces = nearbyPlaces,
                photos = photos
            )
        }

    fun onTypeChange(type:PropertyType){
        state = state.copy(type = type)

    }
    fun onPriceChange(price:Double){
        state = state.copy(price = price)

    }
    fun onAreaChange(area:Int){
        state = state.copy(area = area)
    }
    fun onRoomsChange(rooms: Int) {
        state = state.copy(rooms = rooms)
    }

    fun onBedroomsChange(bedrooms: Int) {
        state = state.copy(bedrooms = bedrooms)
    }

    fun onBathroomsChange(bathrooms: Int) {
        state = state.copy(bathrooms = bathrooms)

    }

    fun onDescriptionChange(description: String) {
        state = state.copy(description = description)
        //println("you have changed the description to ${state.description} and the type is ${state.type}")
    }

    fun onIsSoldChange(isSold: Boolean) {
        state = state.copy(isSold = isSold)
    }

    fun onCreatedDateChange(createDate: Date) {
        state = state.copy(createdDate = createDate)
    }

    fun onSoldDateChange(soldDate: Date) {
        state = state.copy(soldDate = soldDate)
    }

    fun onAgentNameChange(agentName: String) {
        state = state.copy(agentName = agentName)
    }

    fun onStreetChange(street: String) {
        state = state.copy(street = street)
    }

    fun onExtraChange(extra: String) {
        state = state.copy(extra = extra)
    }

    fun onCityChange(city: String) {
        state = state.copy(city = city)
    }

    fun onStateChange(addressState: String) {
        state = state.copy(state = addressState)
    }

    fun onCountryChange(country: String) {
        state = state.copy(country = country)
    }

    fun onPostalCodeChange(postCode: String) {
        state = state.copy(postalCode = postCode)
        println("These are all the values $state")
    }

    fun onLatitudeChange(latitude: Double) {
        state = state.copy(latitude = latitude)
    }

    fun onLongitudeChange(longitude: Double) {
        state = state.copy(longitude = longitude)
    }
    fun onNearbyPlacesChanged(nearbyPlace: NearbyPlacesType){
        var nearbyPlaces = mutableListOf<NearbyPlacesType>()
        state.copy().nearbyPlaces?.map {
            nearbyPlaces.add(it)
        }
        if (nearbyPlaces != null && nearbyPlaces.contains(nearbyPlace)){
            nearbyPlaces.remove(nearbyPlace)
        }else{
            nearbyPlaces.add(nearbyPlace)
        }
        state = state.copy(nearbyPlaces = nearbyPlaces)

        println("These are all the values $state")
    }
    /*fun onPhotosChanged(photos: List<>){
        state = state.copy(nearbyPlaces = nearbyPlaces)
    }*/

    fun addOrUpdateProperty() = viewModelScope.launch(Dispatchers.IO) {
        addPropertyUseCase(property = property)
    }
    private fun checkFormIsValid():Boolean{
            if (state.type == null) return false
            if (state.price == null || state.price!! <= 0.0) return false
            if (state.area == null || state.area!! <= 0) return false
            if (state.rooms == null || state.rooms!! <= 0) return false
            if (state.bedrooms == null || state.bedrooms!! <= 0) return false
            if (state.bathrooms == null || state.bathrooms!! <= 0) return false
            if (state.description.isNullOrBlank()) return false
            if (state.createdDate == null) return false
            if (state.agentName.isNullOrBlank()) return false
            if (state.street.isNullOrBlank()) return false
            if (state.city.isNullOrBlank()) return false
            if (state.state.isNullOrBlank()) return false
            if (state.country.isNullOrBlank()) return false
            if (state.postalCode.isNullOrBlank()) return false

            return true
        }
}

data class AddEditState(
    val id:Long? = null,
    val type: PropertyType? = null,
    val price:Double? = null,
    val area:Int? = null,
    val rooms: Int? = null,
    val bedrooms: Int? = null,
    val bathrooms: Int? = null,
    val description:String? = null,
    val isSold:Boolean = false,
    val createdDate: Date? = null,
    val soldDate: Date? = null,
    val agentName: String? = null,
    val addressId: Long = 0,
    val street: String? = null,
    val extra: String? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val postalCode: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val nearbyPlaces: List<NearbyPlacesType>? = null,
    val photos: List<PropertyPhotosModel>? = null
)