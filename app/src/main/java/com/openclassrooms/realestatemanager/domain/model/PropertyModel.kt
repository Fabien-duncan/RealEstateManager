package com.openclassrooms.realestatemanager.domain.model

import com.openclassrooms.realestatemanager.data.local.model.Address
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import com.openclassrooms.realestatemanager.enums.PropertyType
import java.util.Date

data class PropertyModel(
    val id:Long = 0,
    val type: PropertyType,
    val price:Int,
    val area:Int,
    val rooms: Int,
    val bedrooms: Int,
    val bathrooms: Int,
    val description:String,
    val isSold:Boolean = false,
    val createdDate: Date = Date(),
    val soldDate: Date? = null,
    val agentName: String,
    val address: AddressModel,
    val nearbyPlaces: List<NearbyPlacesType>?,
    val photos: List<PropertyPhotosModel>?
)
