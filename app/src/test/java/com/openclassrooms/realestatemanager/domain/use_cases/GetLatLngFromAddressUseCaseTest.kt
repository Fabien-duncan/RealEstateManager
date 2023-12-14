package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.domain.geocoding.GeocodingRepository
import com.openclassrooms.realestatemanager.domain.geocoding.LatLongEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetLatLngFromAddressUseCaseTest{
    @MockK
    lateinit var mockRepository: GeocodingRepository

    lateinit var getLatLngFromAddressUseCase: GetLatLngFromAddressUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getLatLngFromAddressUseCase = GetLatLngFromAddressUseCase(mockRepository)
    }

    @Test
    fun `invoke should return LatLongEntity`() = runBlocking {
        val mockLatLongEntity = mockk<LatLongEntity>(relaxed = true)
        coEvery {
            mockRepository.getLatLngFromAddress(
                address = any(),
                apiKey = any()
            )
        } returns mockLatLongEntity


        val result = getLatLngFromAddressUseCase(
            address = "https://testAddress",
            apiKey = "123456"
        )

        coVerify { mockRepository.getLatLngFromAddress(any(),any()) }
        assertEquals(mockLatLongEntity, result)
    }
}