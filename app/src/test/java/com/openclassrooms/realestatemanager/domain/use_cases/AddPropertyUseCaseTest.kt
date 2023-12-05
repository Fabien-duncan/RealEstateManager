package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.domain.model.AddressModel
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.repository.Repository
import com.openclassrooms.realestatemanager.enums.PropertyType
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test



class AddPropertyUseCaseTest {
    @MockK
    lateinit var mockRepository: Repository

    lateinit var addPropertyUseCase: AddPropertyUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        addPropertyUseCase = AddPropertyUseCase(mockRepository)
    }
    @Test
    fun `invoke should call insert on the repository`() = runBlocking {
        val property = getTestProperty()
        coEvery { addPropertyUseCase.invoke(property) } returns  property.id

        val id = addPropertyUseCase.invoke(property)

        coVerify { mockRepository.insert(property) }
        assertEquals(property.id, id)
    }


    private fun getTestProperty(): PropertyModel {
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
}