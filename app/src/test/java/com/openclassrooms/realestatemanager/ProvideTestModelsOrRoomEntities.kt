package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.data.local.model.Address
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.data.local.model.PropertyPhotos
import com.openclassrooms.realestatemanager.domain.model.AddressModel
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel
import com.openclassrooms.realestatemanager.enums.PropertyType

object ProvideTestModelsOrRoomEntities {
    fun getTestPropertyModel(): PropertyModel {
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
    fun getTestAddressModel(): AddressModel{
        return AddressModel(
            propertyId = 1,
            number = 73486,
            street = "Sachs Center",
            extra = "apt 7B",
            city = "Jeffersonville",
            state = "Indiana",
            country = "United States",
            postalCode = "47134",
            latitude = 43.63121954424997,
            longitude = 6.662242906927166
        )

    }
    fun getTestPhotosModel():List<PropertyPhotosModel>{
        return listOf(
            PropertyPhotosModel(id = 1, photoPath = "https://images.pexels.com/photos/268533/pexels-photo-268533.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", caption = "view"),
            PropertyPhotosModel(id = 2, photoPath = "https://www.mydomaine.com/thmb/CaWdFGvTH4-h1VvG6tukpKuU2lM=/3409x0/filters:no_upscale():strip_icc()/binary-4--583f06853df78c6f6a9e0b7a.jpeg", caption = "Facade"),
            PropertyPhotosModel(id = 3, photoPath = "https://designingidea.com/wp-content/uploads/2022/01/modern-home-types-of-room-living-space-dining-area-kitchen-glass-door-floor-rug-loft-pendant-light-ss.jpg", caption = null),
            PropertyPhotosModel(id = 4, photoPath = "https://foyr.com/learn/wp-content/uploads/2022/05/foyer-or-entry-hall-types-of-rooms-in-a-house-1024x819.jpg", caption = "entrance"),
        )
    }
    fun getTestPropertyRoomEntity(): Property {
        return Property(
            id = 1L,
            type = PropertyType.HOUSE,
            price = 10000,
            area = 123,
            rooms = 3,
            bedrooms= 1,
            bathrooms = 1,
            description= "stuff",
            agentName = "testAgent",
        )
    }

    fun getTestAddressRoomEntity(): Address {
        return Address(
            propertyId = 1,
            number = 73486,
            street = "Sachs Center",
            extra = "apt 7B",
            city = "Jeffersonville",
            state = "Indiana",
            country = "United States",
            postalCode = "47134",
            latitude = 43.63121954424997,
            longitude = 6.662242906927166
        )
    }

    fun getTestPhotosRoomEntity():List<PropertyPhotos>{
        return listOf(
            PropertyPhotos(propertyId = 2, photoPath = "https://images.pexels.com/photos/268533/pexels-photo-268533.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", caption = "view"),
            PropertyPhotos(propertyId = 2, photoPath = "https://www.mydomaine.com/thmb/CaWdFGvTH4-h1VvG6tukpKuU2lM=/3409x0/filters:no_upscale():strip_icc()/binary-4--583f06853df78c6f6a9e0b7a.jpeg", caption = "Facade"),
            PropertyPhotos(propertyId = 2, photoPath = "https://designingidea.com/wp-content/uploads/2022/01/modern-home-types-of-room-living-space-dining-area-kitchen-glass-door-floor-rug-loft-pendant-light-ss.jpg", caption = null),
            PropertyPhotos(propertyId = 2, photoPath = "https://foyr.com/learn/wp-content/uploads/2022/05/foyer-or-entry-hall-types-of-rooms-in-a-house-1024x819.jpg", caption = "entrance"),
        )
    }
}