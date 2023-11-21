package com.openclassrooms.realestatemanager.presentation.create_edit

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.common.utils.FileUtils
import com.openclassrooms.realestatemanager.domain.geocoding.LatLongEntity
import com.openclassrooms.realestatemanager.domain.model.AddressModel
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel
import com.openclassrooms.realestatemanager.domain.use_cases.AddPropertyUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetAllPropertiesUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetLatLngFromAddressUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetPropertyByIdUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.UpdatePropertyUseCase
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import com.openclassrooms.realestatemanager.enums.PropertyType
import com.openclassrooms.realestatemanager.presentation.detail.DetailSate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    val addPropertyUseCase: AddPropertyUseCase,
    val getPropertyByIdUseCase: GetPropertyByIdUseCase,
    val getLatLngFromAddressUseCase: GetLatLngFromAddressUseCase,
):ViewModel() {
    var state by mutableStateOf(AddEditState())
        private set
    var isAddressValid by mutableStateOf(false)
    private val _isAddOrUpdatePropertyFinished = mutableStateOf(false)
    val isAddOrUpdatePropertyFinished: Boolean
        get() = _isAddOrUpdatePropertyFinished.value
    var _isFormValid by mutableStateOf(false)
        private set
    var mapImageLink by mutableStateOf("")
        private set
    var position by mutableStateOf(LatLongEntity(null,null))
        private set
    private val property: PropertyModel
        get() = state.run {
            PropertyModel(
                id = id,
                price = price!!,
                type = type!!,
                area = area!!,
                rooms = rooms!!,
                bedrooms = bedrooms!!,
                bathrooms = bathrooms!!,
                description = description!!,
                isSold = isSold,
                createdDate = Date(),
                soldDate = if (isSold)soldDate else null,
                agentName = agentName ?: "Fabien Duncan",
                address = AddressModel(
                    propertyId = -1L,
                    number = number!!,
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

    fun resetState(){
        state = state.copy(
            id = -1L,
            price = null,
            type = null,
            area = null,
            rooms = null,
            bedrooms = null,
            bathrooms = null,
            description = null,
            isSold = false,
            createdDate = null,
            soldDate = null,
            agentName = null,
            number = null,
            street = null,
            extra = null,
            city = null,
            state = null,
            country = null,
            postalCode = null,
            latitude = null,
            longitude = null,
            nearbyPlaces = null,
            photos = null
        )
        println("reset state: state.price is now ${state.price}")
    }
    fun onTypeChange(type:PropertyType){
        state = state.copy(type = type)
        setFormIsValid()
    }
    fun onPriceChange(price:String?){
        state = state.copy(price = convertToIntOrNull(price))
        setFormIsValid()
    }
    fun onAreaChange(area:String?){
        state = state.copy(area = convertToIntOrNull(area))
        setFormIsValid()
    }
    fun onRoomsChange(rooms: String?) {
        state = state.copy(rooms = convertToIntOrNull(rooms))
        setFormIsValid()
    }

    fun onBedroomsChange(bedrooms: String?) {
        state = state.copy(bedrooms = convertToIntOrNull(bedrooms))
        setFormIsValid()
    }

    fun onBathroomsChange(bathrooms: String?) {
        state = state.copy(bathrooms = convertToIntOrNull(bathrooms))
        setFormIsValid()
    }

    fun onDescriptionChange(description: String) {
        state = state.copy(description = description)
        setFormIsValid()
        //println("you have changed the description to ${state.description} and the type is ${state.type}")
    }

    fun onIsSoldChange() {
        val isSold = state.isSold
        state = state.copy(isSold = !isSold)
        setFormIsValid()
    }

    fun onCreatedDateChange(createDate: Date) {
        state = state.copy(createdDate = createDate)
        setFormIsValid()
    }

    fun onSoldDateChange(soldDate: Date) {
        state = state.copy(soldDate = soldDate)
        setFormIsValid()
    }

    fun onAgentNameChange(agentName: String) {
        state = state.copy(agentName = agentName)
        setFormIsValid()
    }

    fun onNumberChange(number: String?) {
        state = state.copy(number = convertToIntOrNull(number))
        setFormIsValid()
    }
    fun onStreetChange(street: String) {
        state = state.copy(street = street)
        setFormIsValid()
    }

    fun onExtraChange(extra: String) {
        state = state.copy(extra = extra)
        setFormIsValid()
    }

    fun onCityChange(city: String) {
        state = state.copy(city = city)
        setFormIsValid()
    }

    fun onStateChange(addressState: String) {
        state = state.copy(state = addressState)
        setFormIsValid()
    }

    fun onCountryChange(country: String) {
        state = state.copy(country = country)
        setFormIsValid()
    }

    fun onPostalCodeChange(postCode: String) {
        state = state.copy(postalCode = postCode)
        println("These are all the values $state")
        setFormIsValid()
    }
    fun onPhotoCaptionChanged(caption: String, item:Int) {
        var photos = state.photos?.toMutableList()
        photos?.set(item, photos[item].copy(caption = caption))

        state = state.copy(photos = photos)
        setFormIsValid()
    }
    fun onImagesAdded(originalImagesUris: List<Uri>, context:Context){
        val photosCopy = (state.copy().photos ?: mutableListOf()).toMutableList()
        originalImagesUris.forEach {
            val newUri = FileUtils.copyImageToInternalStorage(it, context)
            val newPhoto = PropertyPhotosModel(photoPath = newUri.toString())

            photosCopy += newPhoto
        }

        println("list of photos $photosCopy")

        state = state.copy(photos = photosCopy)
        setFormIsValid()
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
        setFormIsValid()

        println("These are all the values $state")
    }
    /*fun onPhotosChanged(photos: List<>){
        state = state.copy(nearbyPlaces = nearbyPlaces)
    }*/
    fun onIsAddressValidChanged(){

        isAddressValid = !isAddressValid
        Log.d("addEditViewModel", "changed isAddressValid to $isAddressValid")
        setFormIsValid()
    }

    fun addOrUpdateProperty() = viewModelScope.launch(Dispatchers.IO) {
        if (isAddressValid && position.latitude != null && position.longitude != null){
            state = state.copy(latitude = position.latitude, longitude = position.longitude)
        }
        val id = addPropertyUseCase(property = property)
        //getAllPropertiesUseCase
        state = state.copy(id = id)
        _isAddOrUpdatePropertyFinished.value = true
    }
    fun getLatLongFromAddress(){
        viewModelScope.launch{
            val key = BuildConfig.GMP_key
            val address = "${state.number} ${state.street} ${state.city} ${state.postalCode}"
            val result = getLatLngFromAddressUseCase.invoke(address = address, apiKey = key)
            if (result != null) {
                position = result
                mapImageLink = getMapImage(result.latitude, result.longitude)
            }
            Log.d("addEditViewModel","your lat long is $position and the link to the image is $mapImageLink")
        }
    }

    fun getPropertyById(propertyId:Long) = viewModelScope.launch {
        if (state.id != propertyId){
            println("getting property by Id")
            getPropertyByIdUseCase(propertyId).collectLatest { property ->
                state = state.copy(
                    id = property.id,
                    price = property.price,
                    type = property.type,
                    area = property.area!!,
                    rooms = property.rooms!!,
                    bedrooms = property.bedrooms!!,
                    bathrooms = property.bathrooms!!,
                    description = property.description!!,
                    isSold = property.isSold,
                    createdDate = property.createdDate,
                    soldDate = property.soldDate,
                    agentName = property.agentName,
                    number = property.address.number!!,
                    street = property.address.street!!,
                    extra = property.address.extra,
                    city = property.address.city!!,
                    state = property.address.state!!,
                    country = property.address.country!!,
                    postalCode = property.address.postalCode!!,
                    latitude = property.address.latitude,
                    longitude = property.address.longitude,
                    nearbyPlaces = property.nearbyPlaces,
                    photos = property.photos
                )

            }
            isAddressValid = state.latitude != null && state.longitude != null
        }

    }

    private fun convertToIntOrNull(value:String?):Int? {
        return if (value.isNullOrBlank()) null
        else {
            try {
                value.toInt()
            } catch (e: NumberFormatException) {
                null
            }
        }
    }
    private fun convertToDoubleOrNull(value:String?):Double? {
        return if (value.isNullOrBlank()) null
        else {
            try {
                value.toDouble()
            } catch (e: NumberFormatException) {
                null
            }
        }
    }
    fun resetFinishedState(){
        _isAddOrUpdatePropertyFinished.value = false
    }

    private fun setFormIsValid(){
        _isFormValid = checkFormIsValid()
    }
    private fun checkFormIsValid():Boolean{
        println("Checking form is valid")
        if (state.type == null) return false
        if (state.price == null || state.price!! <= 0.0) return false
        if (state.area == null || state.area!! <= 0) return false
        if (state.rooms == null || state.rooms!! <= 0) return false
        if (state.bedrooms == null || state.bedrooms!! <= 0) return false
        if (state.bathrooms == null || state.bathrooms!! <= 0) return false
        if (state.description.isNullOrBlank()) return false
        if (state.agentName.isNullOrBlank()) return false
        if (state.street.isNullOrBlank()) return false
        if (state.city.isNullOrBlank()) return false
        if (state.state.isNullOrBlank()) return false
        if (state.country.isNullOrBlank()) return false
        if (state.postalCode.isNullOrBlank()) return false

        return true
    }
    private fun getMapImage(lat:Double?, long:Double?):String{
        val key = BuildConfig.GMP_key
        return if (lat != null && long != null){
            "https://maps.googleapis.com/maps/api/staticmap?center=$lat,%20$long&format=jpg&markers=%7C$lat,%20$long&zoom=19&size=800x400&key=$key"
        }
        else{
            ""
        }
    }
}

data class AddEditState(
    val id:Long = -1L,
    val type: PropertyType? = null,
    val price:Int? = null,
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
    val number: Int?=null,
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