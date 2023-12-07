package com.openclassrooms.realestatemanager.data.Connection

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.openclassrooms.realestatemanager.common.utils.VersionProvider
import com.openclassrooms.realestatemanager.domain.Connection.ConnectionCheckerRepository
import javax.inject.Inject

class ConnectionCheckerRepositoryImpl @Inject constructor(
    private val  connectivityManager:ConnectivityManager,
    private val versionProvider: VersionProvider
): ConnectionCheckerRepository {

    @SuppressLint("NewApi")
    override fun isInternetConnected(): Boolean {

        if (versionProvider.getSdkInt() >= Build.VERSION_CODES.M) {
            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false
            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}