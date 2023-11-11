package com.openclassrooms.realestatemanager.domain.mapper

import com.openclassrooms.realestatemanager.data.local.model.Address
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.data.local.model.PropertyNearbyPlaces
import com.openclassrooms.realestatemanager.data.local.model.PropertyPhotos
import com.openclassrooms.realestatemanager.data.local.model.PropertyWithAllDetails
import com.openclassrooms.realestatemanager.domain.model.AddressModel
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType

class PropertyMapper{
    fun mapToRoomEntity(property: PropertyModel) = PropertyWithAllDetails(
        property = propertyModelToRoom(property),
        address = addressModelToRoom(property.address, 0),
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
        photos = property.photos?.let { listOfPropertyPhotosRoomToModel(it) }
    )

    private fun copyNearbyPlacesList(nearbyPlaces: List<PropertyNearbyPlaces>):List<NearbyPlacesType>?{
        var nearbyPlacesType = mutableListOf<NearbyPlacesType>()
        nearbyPlaces.forEach{nearbyPlace ->
            nearbyPlacesType.add(nearbyPlace.nearbyType)}

        return if (nearbyPlaces.isEmpty()) {
            null
        } else {
            nearbyPlacesType
        }
    }
    private fun addressRoomToModel(address: Address) = AddressModel(
            id = address.id,
            propertyId = address.propertyId,
            number = address.number,
            street = address.street,
            extra = address.extra,
            city = address.city,
            state = address.state,
            country = address.country,
            postalCode = address.postalCode,
            latitude = address.latitude,
            longitude = address.longitude
    )
    fun addressModelToRoom(address: AddressModel, propertyId:Long) = Address(
        propertyId = propertyId,
        number = address.number,
        street = address.street,
        city = address.city,
        state = address.state,
        country = address.country,
        postalCode = address.postalCode,
        latitude = address.latitude,
        longitude = address.longitude
    )
    fun propertyModelToRoom(property: PropertyModel):Property {
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
        if (property.id > 0) return roomProperty.copy(id = property.id)
        else return roomProperty
    }
    fun nearbyPlacesToRoomEntities(nearbyPlaces: List<NearbyPlacesType>, propertyId: Long):List<PropertyNearbyPlaces>{

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

    private fun listOfPropertyPhotosRoomToModel(propertyPhotos: List<PropertyPhotos>):List<PropertyPhotosModel>? {
        var photos = mutableListOf<PropertyPhotosModel>()

        propertyPhotos.forEach{
            photos.add(propertyPhotoRoomToModel(it))
        }
        return photos
    }
    private fun propertyPhotoRoomToModel(propertyPhoto: PropertyPhotos) = PropertyPhotosModel(
        id =  propertyPhoto.id,
        photoPath = propertyPhoto.photoPath,
        caption = propertyPhoto.caption
    )

    public fun listOfPropertyPhotosModelToRoom(propertyPhotos:List<PropertyPhotosModel>, propertyId: Long):List<PropertyPhotos> {
        var photos = mutableListOf<PropertyPhotos>()

        propertyPhotos.forEach {
            photos.add(propertyPhotoModelToRoom(it, propertyId))
        }

        return photos
    }
    private fun propertyPhotoModelToRoom(propertyPhoto: PropertyPhotosModel, propertyId: Long) = PropertyPhotos(
        id =  propertyPhoto.id,
        propertyId = propertyId,
        photoPath = propertyPhoto.photoPath,
        caption = propertyPhoto.caption
    )

}