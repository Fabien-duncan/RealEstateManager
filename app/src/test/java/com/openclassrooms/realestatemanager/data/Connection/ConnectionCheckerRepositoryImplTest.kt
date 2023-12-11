package com.openclassrooms.realestatemanager.data.Connection

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import com.openclassrooms.realestatemanager.common.utils.VersionProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class ConnectionCheckerRepositoryImplTes{
    @MockK
    lateinit var connectivityManager: ConnectivityManager
    @MockK
    lateinit var versionProvider: VersionProvider

    private lateinit var connectionCheckerRepository: ConnectionCheckerRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        connectionCheckerRepository = ConnectionCheckerRepositoryImpl(connectivityManager, versionProvider)
    }

    @Test
    fun `isInternetConnected should return true when WiFi is active, cellular is not active and sdk greater or equal to 23`() {
        every { versionProvider.getSdkInt()} returns 25
        val networkCapabilities = mockk<NetworkCapabilities>()
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false

        val activeNetwork = mockk<Network>()
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns networkCapabilities

        val result = connectionCheckerRepository.isInternetConnected()

        assertTrue(result)
    }
    @Test
    fun `isInternetConnected should return true when WiFi is not active, cellular is active and sdk greater or equal to 23`() {
        every { versionProvider.getSdkInt()} returns 25
        val networkCapabilities = mockk<NetworkCapabilities>()
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true

        val activeNetwork = mockk<Network>()
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns networkCapabilities

        val result = connectionCheckerRepository.isInternetConnected()

        assertTrue(result)
    }
    @Test
    fun `isInternetConnected should return true when WiFi is active, cellular is active and sdk greater or equal to 23`() {
        every { versionProvider.getSdkInt()} returns 25
        val networkCapabilities = mockk<NetworkCapabilities>()
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true

        val activeNetwork = mockk<Network>()
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns networkCapabilities

        val result = connectionCheckerRepository.isInternetConnected()

        assertTrue(result)
    }
    @Test
    fun `isInternetConnected should return false when WiFi is not active, cellular is not active and sdk greater or equal to 23`() {
        every { versionProvider.getSdkInt()} returns 25
        val networkCapabilities = mockk<NetworkCapabilities>()
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false

        val activeNetwork = mockk<Network>()
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns networkCapabilities

        val result = connectionCheckerRepository.isInternetConnected()

        assertFalse(result)
    }
    @Test
    fun `isInternetConnected should return true when no active network and sdk smaller than 23`() {
        every { versionProvider.getSdkInt()} returns 22
        val networkInfo = mockk<NetworkInfo>(relaxed = true)
        every { connectivityManager.activeNetworkInfo } returns networkInfo

        every { networkInfo.isConnected} returns true

        val result = connectionCheckerRepository.isInternetConnected()

        assertTrue(result)
    }

    @Test
    fun `isInternetConnected should return false when no active network and sdk smaller than 23`() {
        every { versionProvider.getSdkInt()} returns 22
        val networkInfo = mockk<NetworkInfo>(relaxed = true)
        every { connectivityManager.activeNetworkInfo } returns networkInfo

        every { networkInfo.isConnected} returns false

        val result = connectionCheckerRepository.isInternetConnected()

        assertFalse(result)
    }

}