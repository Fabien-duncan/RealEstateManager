package com.openclassrooms.realestatemanager.domain.mapper

import com.openclassrooms.realestatemanager.data.local.model.PropertyNearbyPlaces
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType

class NearbyPlacesMapper {
    fun mapToRoomEntities(nearbyPlaces: List<NearbyPlacesType>, propertyId: Long):List<PropertyNearbyPlaces>{

        val nearbyPlacesRoom = mutableListOf<PropertyNearbyPlaces>()
        nearbyPlaces.forEach {
            nearbyPlacesRoom.add(
                PropertyNearbyPlaces(
                    propertyId = propertyId,
                    nearbyType = it
                )
            )
        }
        return nearbyPlacesRoom
    }
    fun mapToNearbyPlacesList(nearbyPlaces: List<PropertyNearbyPlaces>):List<NearbyPlacesType>?{
        var nearbyPlacesType = mutableListOf<NearbyPlacesType>()
        nearbyPlaces.forEach{nearbyPlace ->
            nearbyPlacesType.add(nearbyPlace.nearbyType)}

        return if (nearbyPlaces.isEmpty()) {
            null
        } else {
            nearbyPlacesType
        }
    }
}