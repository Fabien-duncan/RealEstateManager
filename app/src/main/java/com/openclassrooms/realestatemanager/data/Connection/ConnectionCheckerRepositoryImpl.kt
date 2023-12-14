package com.openclassrooms.realestatemanager.data.Connection

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.openclassrooms.realestatemanager.common.utils.VersionProvider
import com.openclassrooms.realestatemanager.domain.Connection.ConnectionCheckerRepository
import javax.inject.Inject

/**
 * Implementation of [ConnectionCheckerRepository] that checks the device's internet connectivity.
 *
 * @param connectivityManager The system service providing information about device connectivity.
 * @param versionProvider The provider for retrieving the Android SDK version.
 */
class ConnectionCheckerRepositoryImpl @Inject constructor(
    private val  connectivityManager:ConnectivityManager,
    private val versionProvider: VersionProvider
): ConnectionCheckerRepository {

    /**
     * Checks if the device has an active internet connection.
     *
     * @return `true` if the device is connected to the internet, `false` otherwise.
     */
    @SuppressLint("NewApi")
    override fun isInternetConnected(): Boolean {

        if (versionProvider.getSdkInt() >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
        //for later sdk versions
        else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}