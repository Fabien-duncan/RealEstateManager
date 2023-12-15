package com.openclassrooms.realestatemanager.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.common.utils.NumberUtils
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import com.openclassrooms.realestatemanager.enums.PropertyType
import java.util.Date
/**
 * ViewModel class for managing filter-related operations.
 */
class FilterViewModel:ViewModel() {
    /**
     * The current state of the filter.
     */
    var state by mutableStateOf(FilterState())
        private set
    /**
     * Clears the filter state, resetting all filter parameters to their default values.
     */
    fun clearFilterState(){
        state = FilterState()
    }
    //-------------------------------------------------------------------------------------------------//
    // The Following methods are for handling the change in the filter options in the form in the View //
    //-------------------------------------------------------------------------------------------------//
    fun onAgentChanged(agentName: String?){
        state = state.copy(agentName = agentName)
    }
    fun onTypeChange(type:PropertyType?){
        state = state.copy(propertyType = type)
    }
    fun onMinPriceChange(number: String?) {
        state = state.copy(minPrice = NumberUtils.convertToIntOrNull(number))
    }
    fun onMaxPriceChange(number: String?) {
        state = state.copy(maxPrice = NumberUtils.convertToIntOrNull(number))
    }
    fun onMinSurfaceChange(number: String?) {
        state = state.copy(minSurface = NumberUtils.convertToIntOrNull(number))
    }
    fun onMaxSurfaceChange(number: String?) {
        state = state.copy(maxSurface = NumberUtils.convertToIntOrNull(number))
    }
    fun onMinRoomsChange(number: String?) {
        state = state.copy(minRooms = NumberUtils.convertToIntOrNull(number))
    }
    fun onMaxRoomsChange(number: String?) {
        state = state.copy(maxRooms = NumberUtils.convertToIntOrNull(number))
    }
    fun onMinBathroomsChange(number: String?) {
        state = state.copy(minBathrooms = NumberUtils.convertToIntOrNull(number))
    }
    fun onMaxBathroomsChange(number: String?) {
        state = state.copy(maxBathrooms = NumberUtils.convertToIntOrNull(number))
    }
    fun onMinBedroomsChange(number: String?) {
        state = state.copy(minBedrooms = NumberUtils.convertToIntOrNull(number))
    }
    fun onMaxBedroomsChange(number: String?) {
        state = state.copy(maxBedrooms = NumberUtils.convertToIntOrNull(number))
    }
    fun onMinPhotosChange(number: String?) {
        state = state.copy(minPictures = NumberUtils.convertToIntOrNull(number))
    }
    fun onMinCreatedDateChanged(date: Date?):Boolean{
        state = state.copy(minCreationDate = date)
        return checkCreatedDatesAreValid()
    }
    fun onMaxCreatedDateChanged(date: Date?):Boolean{
        state = state.copy(maxCreationDate = date)
        return checkCreatedDatesAreValid()
    }
    fun onIsSoldChanged(isSold: Boolean?){
        state = state.copy(isSold = isSold)
    }
    fun onMinSoldDateChanged(date: Date?):Boolean{
        state = state.copy(minSoldDate = date)
        return checkSoldDatesAreValid()
    }
    fun onMaxSoldDateChanged(date: Date?):Boolean{
        state = state.copy(maxSoldDate = date)
        return checkSoldDatesAreValid()
    }
    fun onNearbyPlacesChanged(nearbyPlace: NearbyPlacesType){
        val nearbyPlaces = mutableListOf<NearbyPlacesType>()
        state.copy().nearbyPlaces?.map {
            nearbyPlaces.add(it)
        }
        if (nearbyPlaces.contains(nearbyPlace)){
            nearbyPlaces.remove(nearbyPlace)
        }else{
            nearbyPlaces.add(nearbyPlace)
        }
        state = state.copy(nearbyPlaces = nearbyPlaces)
    }
    /**
     * Checks if the selected creation dates are valid (min date is before max date).
     *
     * @return True if the selected creation dates are valid, false otherwise.
     */
    private fun checkCreatedDatesAreValid():Boolean{
        return if (state.minCreationDate != null && state.maxCreationDate != null) state.minCreationDate!!.before(state.maxCreationDate) else true
    }

    /**
     * Checks if the selected sold dates are valid (min date is before max date).
     *
     * @return True if the selected sold dates are valid, false otherwise.
     */
    private fun checkSoldDatesAreValid():Boolean{
        return if (state.minCreationDate != null && state.maxCreationDate != null) state.minCreationDate!!.before(state.maxCreationDate) else true
    }

}

/**
 * Data class representing the state of the filter.
 */
data class FilterState(
    val agentName:String?=null,
    val propertyType: PropertyType? = null,
    val minPrice:Int?=null,
    val maxPrice:Int?=null,
    val minSurface:Int?=null,
    val maxSurface:Int?=null,
    val minRooms:Int?=null,
    val maxRooms:Int?=null,
    val minBathrooms:Int?=null,
    val maxBathrooms:Int?=null,
    val minBedrooms:Int?=null,
    val maxBedrooms:Int?=null,
    val minPictures:Int?=null,
    val minCreationDate:Date?=null,
    val maxCreationDate:Date?=null,
    val isSold:Boolean?=null,
    val minSoldDate:Date?=null,
    val maxSoldDate:Date?=null,
    val nearbyPlaces:List<NearbyPlacesType>?=null,
    )

