package com.openclassrooms.realestatemanager.domain.use_cases

import android.location.Location
import com.openclassrooms.realestatemanager.domain.location.LocationTracker
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetCurrentLocationUseCaseTest{
    @MockK
    lateinit var mockRepository: LocationTracker

    lateinit var getCurrentLocationUseCase: GetCurrentLocationUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getCurrentLocationUseCase = GetCurrentLocationUseCase(mockRepository)
    }
    @Test
    fun `invoke should return current location`() = runBlocking {

        val mockLocation = mockk<Location>(relaxed = true)
        coEvery { mockRepository.getCurrentLocation() } returns mockLocation

        val result = getCurrentLocationUseCase()

        coVerify { mockRepository.getCurrentLocation() }
        TestCase.assertEquals(mockLocation, result)
    }
}