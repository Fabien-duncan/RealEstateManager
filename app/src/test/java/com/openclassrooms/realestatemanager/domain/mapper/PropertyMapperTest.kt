package com.openclassrooms.realestatemanager.domain.mapper

import com.openclassrooms.realestatemanager.ProvideTestModelsOrRoomEntities
import com.openclassrooms.realestatemanager.data.local.model.PropertyWithAllDetails
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class PropertyMapperTest{
    private lateinit var propertyMapper: PropertyMapper

    @Before
    fun setUp() {
        propertyMapper = PropertyMapper(PropertyPhotosMapper(), NearbyPlacesMapper(), AddressMapper())
    }

    @Test
    fun `mapToDomainModel should map PropertyWithAllDetails to PropertyModel`() {
        // Mock data
        val propertyWithAllDetails = PropertyWithAllDetails(
            property = ProvideTestModelsOrRoomEntities.getTestPropertyRoomEntity(),
            address = ProvideTestModelsOrRoomEntities.getTestAddressRoomEntity(),
            nearbyPlaces = null,
            photos = null
        )

        val result = propertyMapper.mapToDomainModel(propertyWithAllDetails)

        assertEquals(propertyWithAllDetails.property.id, result.id)
        assertEquals(propertyWithAllDetails.property.type, result.type)
        assertEquals(propertyWithAllDetails.address.country, result.address.country)
    }

    @Test
    fun `mapToRoomEntity should map PropertyModel to Property`() {
        val propertyModel = ProvideTestModelsOrRoomEntities.getTestPropertyModel()

        val result = propertyMapper.mapToRoomEntity(propertyModel)

        assertEquals(propertyModel.type, result.type)
    }
}