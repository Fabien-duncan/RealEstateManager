package com.openclassrooms.realestatemanager

import android.content.Context
import android.net.wifi.WifiManager
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class TestIsInternetAvailableUtils {
    @MockK
    lateinit var context: Context
    @MockK
    lateinit var wifiManager: WifiManager

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { context.getSystemService(Context.WIFI_SERVICE) } returns wifiManager
    }

    @Test
    fun `testIsInternetAvailable should return true when WiFi is enabled`() {
        every { wifiManager.isWifiEnabled } returns true

        val isInternetAvailable = Utils.isInternetAvailable(context)

        assertEquals(true, isInternetAvailable)
    }

    @Test
    fun `testIsInternetAvailable should return false when WiFi is enabled`() {
        every { wifiManager.isWifiEnabled } returns true

        val isInternetAvailable = Utils.isInternetAvailable(context)

        assertEquals(true, isInternetAvailable)
    }
}