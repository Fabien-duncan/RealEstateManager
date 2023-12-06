package com.openclassrooms.realestatemanager.domain.mapper

import com.openclassrooms.realestatemanager.data.local.model.Address
import com.openclassrooms.realestatemanager.domain.model.AddressModel

class AddressMapper {
    fun mapToRoomEntity(address: AddressModel, propertyId:Long) = Address(
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
    fun mapToDomainModel(address: Address) = AddressModel(
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
}