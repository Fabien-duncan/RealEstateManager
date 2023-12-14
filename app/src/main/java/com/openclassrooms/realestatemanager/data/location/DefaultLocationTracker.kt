package com.openclassrooms.realestatemanager.data.location

import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.openclassrooms.realestatemanager.common.utils.PermissionCheckProvider
import com.openclassrooms.realestatemanager.domain.location.LocationTracker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Default implementation of [LocationTracker] that uses the Fused Location Provider
 * to retrieve the last known location.
 *
 * @property fusedLocationProviderClient FusedLocationProviderClient for accessing location services.
 * @property application The application context.
 * @property permissionCheckProvider PermissionCheckProvider for checking and requesting location permissions.
 */
class DefaultLocationTracker (
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val application: Application,
    private val permissionCheckProvider: PermissionCheckProvider
) : LocationTracker {
    /**
     * Retrieves the current device location asynchronously.
     *
     * @return [Location] object representing the current device location, or null if location
     * access is not granted or if the location retrieval fails.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getCurrentLocation(): Location? {
        val hasAccessFineLocationPermission = permissionCheckProvider.checkSelfPermission(
            application,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCoarseLocationPermission = permissionCheckProvider.checkSelfPermission(
            application,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val isGpsEnabled = permissionCheckProvider.isGpsEnabled(application)

        if (!isGpsEnabled && !(hasAccessCoarseLocationPermission || hasAccessFineLocationPermission)) {
            return null
        }
        return suspendCancellableCoroutine { cont ->
            fusedLocationProviderClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        cont.resume(result) {}
                    } else {
                        cont.resume(null) {}
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    cont.resume(it) {}
                }
                addOnFailureListener {
                    cont.resume(null) {}
                }
                addOnCanceledListener {
                    cont.cancel()
                }
            }
        }
    }
}