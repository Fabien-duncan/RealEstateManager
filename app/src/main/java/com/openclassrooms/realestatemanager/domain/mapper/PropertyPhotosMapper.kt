package com.openclassrooms.realestatemanager.domain.mapper

import com.openclassrooms.realestatemanager.data.local.model.PropertyPhotos
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel

class PropertyPhotosMapper {
    fun mapToRoomEntities(propertyPhotos:List<PropertyPhotosModel>, propertyId: Long):List<PropertyPhotos> {
        var photos = mutableListOf<PropertyPhotos>()

        propertyPhotos.forEach {
            photos.add(mapToRoomEntity(it, propertyId))
        }

        return photos
    }
    private fun mapToRoomEntity(propertyPhoto: PropertyPhotosModel, propertyId: Long) = PropertyPhotos(
        id =  propertyPhoto.id,
        propertyId = propertyId,
        photoPath = propertyPhoto.photoPath,
        caption = propertyPhoto.caption
    )
    fun mapToListOfDomainModels(propertyPhotos: List<PropertyPhotos>):List<PropertyPhotosModel>? {
        var photos = mutableListOf<PropertyPhotosModel>()

        propertyPhotos.forEach{
            photos.add(mapToDomainModel(it))
        }
        return photos
    }
    private fun mapToDomainModel(propertyPhoto: PropertyPhotos) = PropertyPhotosModel(
        id =  propertyPhoto.id,
        photoPath = propertyPhoto.photoPath,
        caption = propertyPhoto.caption
    )
}