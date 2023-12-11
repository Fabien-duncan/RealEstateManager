package com.openclassrooms.realestatemanager.domain.mapper

import com.openclassrooms.realestatemanager.ProvideTestModelsOrRoomEntities
import junit.framework.TestCase.assertEquals
import org.junit.Test

class AddressMapperTest{
    private val addressMapper = AddressMapper()

    @Test
    fun `mapToRoomEntity should map AddressModel to Address entity`() {
        val addressModel = ProvideTestModelsOrRoomEntities.getTestAddressModel()
        val propertyId = 1L

        val result = addressMapper.mapToRoomEntity(addressModel, propertyId)

        assertEquals(propertyId, result.propertyId)
        assertEquals(addressModel.number, result.number)
        assertEquals(addressModel.street, result.street)
        assertEquals(addressModel.city, result.city)
        assertEquals(addressModel.state, result.state)
        assertEquals(addressModel.country, result.country)
        assertEquals(addressModel.postalCode, result.postalCode)
    }

    @Test
    fun `mapToDomainModel should map Address entity to AddressModel`() {
        val addressEntity = ProvideTestModelsOrRoomEntities.getTestAddressRoomEntity()

        val result = addressMapper.mapToDomainModel(addressEntity)

        assertEquals(addressEntity.id, result.id)
        assertEquals(addressEntity.propertyId, result.propertyId)
        assertEquals(addressEntity.number, result.number)
        assertEquals(addressEntity.street, result.street)
        assertEquals(addressEntity.extra, result.extra)
        assertEquals(addressEntity.city, result.city)
        assertEquals(addressEntity.state, result.state)
        assertEquals(addressEntity.country, result.country)
        assertEquals(addressEntity.postalCode, result.postalCode)
    }

}