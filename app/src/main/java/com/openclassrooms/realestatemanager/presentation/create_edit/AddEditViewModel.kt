package com.openclassrooms.realestatemanager.presentation.create_edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.room.ColumnInfo
import com.openclassrooms.realestatemanager.domain.model.AddressModel
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel
import com.openclassrooms.realestatemanager.domain.use_cases.AddPropertyUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.UpdatePropertyUseCase
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import com.openclassrooms.realestatemanager.enums.PropertyType
import com.openclassrooms.realestatemanager.presentation.detail.DetailSate
import java.util.Date
import javax.inject.Inject

class AddEditViewModel @Inject constructor(
    val addPropertyUseCase: AddPropertyUseCase,
    val updatePropertyUseCase: UpdatePropertyUseCase
):ViewModel() {
    var state by mutableStateOf(AddEditState())
        private set
    val isFormValid: Boolean
        get() = checkFormIsValid()

    private fun checkFormIsValid() =
        state.isSold != null &&
        state.price != null &&
        state != null
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