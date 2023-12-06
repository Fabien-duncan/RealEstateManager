package com.openclassrooms.realestatemanager.domain.mapper

import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.data.local.model.PropertyWithAllDetails
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import javax.inject.Inject

class PropertyMapper @Inject constructor(
    private val propertyPhotosMapper: PropertyPhotosMapper,
    private val nearbyPlacesMapper: NearbyPlacesMapper,
    private val addressMapper: AddressMapper
){
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
        address = addressMapper.mapToDomainModel(property.address),
        nearbyPlaces = property.nearbyPlaces?.let { nearbyPlacesMapper.mapToNearbyPlacesList(it) },
        photos = property.photos?.let { propertyPhotosMapper.mapToListOfDomainModels(it) }
    )
    fun mapToRoomEntity(property: PropertyModel):Property {
        val roomProperty = Property(
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
        return if (property.id > 0) roomProperty.copy(id = property.id)
        else roomProperty
    }
}