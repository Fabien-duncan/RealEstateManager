package com.openclassrooms.realestatemanager.data.repository

import com.openclassrooms.realestatemanager.data.local.PropertyDao
import com.openclassrooms.realestatemanager.data.local.model.Address
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.data.local.model.PropertyNearbyPlaces
import com.openclassrooms.realestatemanager.data.local.model.PropertyPhotos
import com.openclassrooms.realestatemanager.data.local.model.PropertyWithAllDetails
import com.openclassrooms.realestatemanager.domain.mapper.AddressMapper
import com.openclassrooms.realestatemanager.domain.mapper.NearbyPlacesMapper
import com.openclassrooms.realestatemanager.domain.mapper.PropertyMapper
import com.openclassrooms.realestatemanager.domain.mapper.PropertyPhotosMapper
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.enums.PropertyType
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class PropertyRepositoryImplTest{
    @MockK
    lateinit var mockPropertyDao: PropertyDao
    @MockK
    lateinit var mockPropertyMapper: PropertyMapper
    @MockK
    lateinit var mockAddressMapper: AddressMapper
    @MockK
    lateinit var mockNearbyPlacesMapper: NearbyPlacesMapper
    @MockK
    lateinit var mockPropertyPhotosMapper: PropertyPhotosMapper

    lateinit var  propertyRepository: PropertyRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        propertyRepository = PropertyRepositoryImpl(mockPropertyDao,mockPropertyMapper, mockAddressMapper, mockNearbyPlacesMapper, mockPropertyPhotosMapper)
    }

    @Test
    fun `insert should return property ID`() = runBlocking {

        val mockPropertyModel = mockk<PropertyModel>(relaxed = true)
        val mockPropertyRoomEntity = mockk<Property>(relaxed = true)
        val mockAddressRoomEntity = mockk<Address>(relaxed = true)
        val mockNearbyPlacesRoomEntities = mockk<List<PropertyNearbyPlaces>>(relaxed = true)
        val mockPhotos = mockk<List<PropertyPhotos>>(relaxed = true)

        coEvery { mockPropertyMapper.mapToRoomEntity(any()) } returns mockPropertyRoomEntity
        coEvery { mockAddressMapper.mapToRoomEntity(any(), any()) } returns mockAddressRoomEntity
        coEvery { mockNearbyPlacesMapper.mapToRoomEntities(any(), any()) } returns mockNearbyPlacesRoomEntities
        coEvery { mockPropertyPhotosMapper.mapToRoomEntities(any(), any()) } returns mockPhotos
        coEvery { mockPropertyDao.insert(mockAddressRoomEntity) } returns 2L
        coEvery { mockPropertyDao.insertNearbyPlaces(mockNearbyPlacesRoomEntities) } returns listOf(1L,2L)
        coEvery { mockPropertyDao.insert(mockPropertyRoomEntity) } returns 1L
        coEvery { mockPropertyDao.clearNearbyPlacesForProperty(any(), any()) } returns Unit
        coEvery { mockPropertyDao.insert(mockPhotos) } returns Unit

        val result = propertyRepository.insert(mockPropertyModel)

        assertEquals(1L, result)
    }
    @Test
    fun `getPropertyWithDetailsById should return property`() = runBlocking {
        val mockPropertyRoomWithDetails = mockk<PropertyWithAllDetails>(relaxed = true)
        val mockPropertyModel = mockk<PropertyModel>(relaxed = true)

        coEvery { mockPropertyDao.getPropertyWithDetailsById(any()) } returns flowOf(mockPropertyRoomWithDetails)
        coEvery { mockPropertyMapper.mapToDomainModel(any()) } returns mockPropertyModel

        val resultFlow = propertyRepository.getPropertyWithDetailsById(1L)

        resultFlow.collect { result -> assertEquals(mockPropertyModel, result)}
    }
    @Test
    fun `getAllPropertiesWithDetails should return list of properties`() = runBlocking {
        val mockPropertyRoomWithDetailsList = listOf(mockk<PropertyWithAllDetails>(relaxed = true))
        val mockPropertyModelList = listOf(mockk<PropertyModel>(relaxed = true))

        coEvery { mockPropertyDao.getAllProperties() } returns flowOf(mockPropertyRoomWithDetailsList)
        coEvery { mockPropertyMapper.mapToDomainModel(any()) } returnsMany mockPropertyModelList

        val resultFlow = propertyRepository.getAllPropertiesWithDetails()

        resultFlow.collect { result -> assertEquals(mockPropertyModelList, result) }
    }

    @Test
    fun `getFilteredProperties should return filtered properties`() = runBlocking {
        val mockPropertyRoomWithDetailsList = listOf(mockk<PropertyWithAllDetails>(relaxed = true))
        val mockPropertyModelList = listOf(mockk<PropertyModel>(relaxed = true))

        coEvery { mockPropertyDao.getFilterProperties(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns flowOf(mockPropertyRoomWithDetailsList)
        coEvery { mockPropertyMapper.mapToDomainModel(any()) } returnsMany mockPropertyModelList

        val resultFlow = propertyRepository.getFilteredProperties(
            agentName = "test Agent",
            propertyType = PropertyType.APARTMENT,
            minPrice = 100000,
            maxPrice = 200000,
        )

        resultFlow.collect { result -> assertEquals(mockPropertyModelList, result)}
    }
}