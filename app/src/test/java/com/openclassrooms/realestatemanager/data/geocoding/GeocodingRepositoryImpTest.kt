package com.openclassrooms.realestatemanager.data.geocoding

import com.openclassrooms.realestatemanager.data.api.GeocodingApiService
import com.openclassrooms.realestatemanager.data.geocoding.response.GeocodingLocation
import com.openclassrooms.realestatemanager.data.geocoding.response.GeocodingResponse
import com.openclassrooms.realestatemanager.data.geocoding.response.GeocodingResult
import com.openclassrooms.realestatemanager.domain.geocoding.LatLongEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GeocodingRepositoryImpTest{
    @MockK
    lateinit var mockGeocoderApi: GeocodingApiService

    private lateinit var geocodingRepositoryImp: GeocodingRepositoryImp

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        geocodingRepositoryImp = GeocodingRepositoryImp(mockGeocoderApi)
    }

    @Test
    fun `getLatLngFromAddress returns correct LatLongEntity on success`() = runBlocking {
        val address = "1600 Amphitheatre Parkway, Mountain View, CA"
        val apiKey = "testAPEkey"
        val response = mockk<GeocodingResponse>(relaxed = true)
        val geocodingResult = mockk<GeocodingResult>(relaxed = true)
        val location = mockk<GeocodingLocation>(relaxed = true)

        coEvery {mockGeocoderApi.getLatLngFromAddress(address, apiKey)} returns response
        coEvery {response.results[0]} returns geocodingResult
        coEvery {geocodingResult.geometry.location} returns location
        coEvery {location.lat} returns 37.4224764
        coEvery {location.lng} returns -122.0842499

        val result = geocodingRepositoryImp.getLatLngFromAddress(address, apiKey)

        coVerify { mockGeocoderApi.getLatLngFromAddress(any(), any()) }
        assertEquals(LatLongEntity(37.4224764, -122.0842499), result)
    }

    @Test
    fun `getLatLngFromAddress returns null on exception`() = runBlocking {
        val address = "1600 Amphitheatre Parkway, Mountain View, CA"
        val apiKey = "testAPEkey"

        coEvery {mockGeocoderApi.getLatLngFromAddress(address, apiKey)} throws Exception("Some error")


        val result = geocodingRepositoryImp.getLatLngFromAddress(address, apiKey)

        // Then
        coVerify { mockGeocoderApi.getLatLngFromAddress(any(), any()) }
        assertNull(result)
    }


}