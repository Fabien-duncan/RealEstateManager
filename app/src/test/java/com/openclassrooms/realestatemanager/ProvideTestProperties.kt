package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.domain.model.AddressModel
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.enums.PropertyType

object ProvideTestProperties {
    fun getTestProperty(): PropertyModel {
        return PropertyModel(
            id = 1L,
            type = PropertyType.HOUSE,
            price = 10000,
            area = 123,
            rooms = 3,
            bedrooms= 1,
            bathrooms = 1,
            description= "stuff",
            agentName = "testAgent",
            address = AddressModel(propertyId = 1, number = 73486, street = "Sachs Center", extra = "apt 7B", city = "Jeffersonville", state = "Indiana", country = "United States", postalCode = "47134", latitude = 43.63121954424997, longitude = 6.662242906927166),
            nearbyPlaces = null,
            photos = null
        )
    }
     fun getTestProperties(): List<PropertyModel> {
        return listOf(
            PropertyModel(
                id = 1L,
                type = PropertyType.HOUSE,
                price = 10000,
                area = 123,
                rooms = 3,
                bedrooms= 1,
                bathrooms = 1,
                description= "stuff",
                agentName = "testAgent",
                address = AddressModel(propertyId = 1, number = 73486, street = "Sachs Center", extra = "apt 7B", city = "Jeffersonville", state = "Indiana", country = "United States", postalCode = "47134", latitude = 43.63121954424997, longitude = 6.662242906927166),
                nearbyPlaces = null,
                photos = null
            ),
            PropertyModel(
                id = 2L,
                type = PropertyType.CONDO,
                price = 60000,
                area = 203,
                rooms = 6,
                bedrooms= 3,
                bathrooms = 2,
                description= "more stuff",
                agentName = "testAgent",
                address = AddressModel(propertyId = 2, number = 123, street = "first street", extra = "apt A", city = "NewYork", state = "Indiana", country = "United States", postalCode = "33268", latitude = null, longitude = null),
                nearbyPlaces = null,
                photos = null
            ),
        )
    }
}