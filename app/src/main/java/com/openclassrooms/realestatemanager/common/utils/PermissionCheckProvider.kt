package com.openclassrooms.realestatemanager.common.utils

import android.app.Application
import android.content.Context
import android.location.LocationManager
import androidx.core.content.ContextCompat

/**
 * Utility class for providing permissions and GPS status in the application to help with testability.
 */
class PermissionCheckProvider {

    /**
     * Checks if the application has the specified permission.
     *
     * @param application The application instance.
     * @param permission The permission to check.
     * @return Returns if the permission is granted or not granted.
     */
   fun checkSelfPermission(application:Application, permission: String) = ContextCompat.checkSelfPermission(
       application,
       permission
   )
    /**
     * Checks if GPS is enabled on the device.
     *
     * @param application The application instance.
     * @return Returns true if GPS is enabled, otherwise false.
     */
   fun isGpsEnabled(application: Application):Boolean {
       val locationManager = application.getSystemService(
           Context.LOCATION_SERVICE
       ) as LocationManager
       return locationManager
           .isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
               locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
   }
}