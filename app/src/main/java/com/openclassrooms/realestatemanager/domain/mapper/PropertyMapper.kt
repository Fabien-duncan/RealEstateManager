package com.openclassrooms.realestatemanager.domain.mapper

import androidx.room.ColumnInfo
import com.openclassrooms.realestatemanager.data.local.model.Address
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.data.local.model.PropertyNearbyPlaces
import com.openclassrooms.realestatemanager.data.local.model.PropertyWithAllDetails
import com.openclassrooms.realestatemanager.domain.model.AddressModel
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType

class PropertyMapper {
    fun mapToRoomEntity(property: PropertyModel) = PropertyWithAllDetails(
        property = propertyModelToRomm(property),
        address = addressModelToRoom(property.address),
        nearbyPlaces = null,
        photos = null,
    )

    fun mapToDomainModel(property: PropertyWithAllDetails) = PropertyModel(
        id = property.property.id,
        type = property.property.type,
        price = property.property.price,
        area = property.property.area,
        rooms = property.property.rooms,
        bedrooms = property.property.bedrooms,
        bathrooms = property.property.bathrooms,
        description = property.property.description,
        isSold = property.property.isSold,
        createdDate = property.property.createdDate,
        soldDate = property.property.soldDate,
        agentName = property.property.agentName,
        address = addressRoomToModel(property.address),
        nearbyPlaces = property.nearbyPlaces?.let { copyNearbyPlacesList(it) },
        photos = null,

    )

    private fun copyNearbyPlacesList(nearbyPlaces: List<PropertyNearbyPlaces>):List<NearbyPlacesType>{
        var nearbyPlacesType = mutableListOf<NearbyPlacesType>()
        nearbyPlaces.forEach{nearbyPlace ->
            nearbyPlacesType.add(nearbyPlace.type)}
        return nearbyPlacesType
    }
    private fun addressRoomToModel(address: Address) = AddressModel(
            id = address.id,
            propertyId = address.propertyId,
            street = address.street,
            city = address.city,
            state = address.state,
            country = address.country,
            postalCode = address.postalCode,
            latitude = address.latitude,
            longitude = address.longitude
    )
    private fun addressModelToRoom(address: AddressModel) = Address(
        id = address.id,
        propertyId = address.propertyId,
        street = address.street,
        city = address.city,
        state = address.state,
        country = address.country,
        postalCode = address.postalCode,
        latitude = address.latitude,
        longitude = address.longitude
    )
    private fun propertyModelToRomm(property: PropertyModel) = Property(
        id = property.id,
        type = property.type,
        price = property.price,
        area = property.area,
        rooms = property.rooms,
        bedrooms = property.bedrooms,
        bathrooms = property.bathrooms,
        description = property.description,
        isSold = property.isSold,
        createdDate = property.createdDate,
        soldDate = property.soldDate,
        agentName = property.agentName,
    )

}