package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.ProvideTestProperties
import com.openclassrooms.realestatemanager.domain.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetFilteredPropertiesUseCaseTest{
    @MockK
    lateinit var mockRepository: Repository

    lateinit var getFilteredPropertiesUseCase: GetFilteredPropertiesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getFilteredPropertiesUseCase = GetFilteredPropertiesUseCase(mockRepository)
    }
    @Test
    fun `invoke should return filtered properties`() = runBlocking {

        val mockFilteredPropertiesList = ProvideTestProperties.getTestProperties()
        coEvery {
            mockRepository.getFilteredProperties(
                agentName = any(),
                propertyType = any(),
                minPrice = any(),
                maxPrice = any(),
                minSurfaceArea = any(),
                maxSurfaceArea = any(),
                minNumRooms = any(),
                maxNumRooms = any(),
                minNumBathrooms = any(),
                maxNumBathrooms = any(),
                minNumBedrooms = any(),
                maxNumBedrooms = any(),
                minCreationDate = any(),
                maxCreationDate = any(),
                isSold = any(),
                minSoldDate = any(),
                maxSoldDate = any(),
                minNumPictures = any(),
                nearbyPlaceTypes = any()
            )
        } returns flowOf(mockFilteredPropertiesList)

        // When
        val resultFlow = getFilteredPropertiesUseCase(
            minPrice = 10000,
            minNumBedrooms = 2,
            minNumRooms = 4
        )
        coVerify { mockRepository.getFilteredProperties(
            agentName = any(),
            propertyType = any(),
            minPrice = any(),
            maxPrice = any(),
            minSurfaceArea = any(),
            maxSurfaceArea = any(),
            minNumRooms = any(),
            maxNumRooms = any(),
            minNumBathrooms = any(),
            maxNumBathrooms = any(),
            minNumBedrooms = any(),
            maxNumBedrooms = any(),
            minCreationDate = any(),
            maxCreationDate = any(),
            isSold = any(),
            minSoldDate = any(),
            maxSoldDate = any(),
            minNumPictures = any(),
            nearbyPlaceTypes = any()
        ) }
        assertEquals(mockFilteredPropertiesList, resultFlow.single())
    }
}