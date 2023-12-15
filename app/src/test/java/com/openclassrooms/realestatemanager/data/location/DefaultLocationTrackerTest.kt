package com.openclassrooms.realestatemanager.data.location

import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.openclassrooms.realestatemanager.common.utils.PermissionCheckProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class DefaultLocationTrackerTest{
    @MockK
    lateinit var mockFusedLocationProviderClient: FusedLocationProviderClient
    @MockK
    lateinit var mockApplication: Application
    @MockK
    lateinit var mockPermissionCheckProvider: PermissionCheckProvider

    private lateinit var defaultLocationTracker: DefaultLocationTracker

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        defaultLocationTracker = DefaultLocationTracker(mockFusedLocationProviderClient, mockApplication, mockPermissionCheckProvider)
    }
    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getCurrentLocation returns null when GPS is not enabled and no location permission`() = runBlocking {
        every { mockPermissionCheckProvider.checkSelfPermission(any(), any()) } returns  PackageManager.PERMISSION_DENIED
        every { mockPermissionCheckProvider.isGpsEnabled(any()) } returns  false

        val result = defaultLocationTracker.getCurrentLocation()

        assert(result == null)

        verify(exactly = 0) { mockFusedLocationProviderClient.lastLocation }
    }

    @Test
    fun `getCurrentLocation returns location when GPS is enabled and has location permission`() = runBlocking {
        every { mockPermissionCheckProvider.checkSelfPermission(any(), any()) } returns  PackageManager.PERMISSION_GRANTED
        every { mockPermissionCheckProvider.isGpsEnabled(any()) } returns  true

        val mockLocation: Location = mockk(relaxed = true)
        every { mockFusedLocationProviderClient.lastLocation.isComplete } returns true
        every { mockFusedLocationProviderClient.lastLocation.isSuccessful } returns true
        every { mockFusedLocationProviderClient.lastLocation.result } returns mockLocation


        val result = defaultLocationTracker.getCurrentLocation()

        assert(result == mockLocation)

        verify(exactly = 1) { mockFusedLocationProviderClient.lastLocation }
    }
}