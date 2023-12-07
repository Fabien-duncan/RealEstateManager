package com.openclassrooms.realestatemanager.common.utils

import android.app.Application
import android.content.Context
import android.location.LocationManager
import androidx.core.content.ContextCompat


class PermissionCheckProvider {

   fun checkSelfPermission(application:Application, permission: String) = ContextCompat.checkSelfPermission(
       application,
       permission
   )
   fun isGpsEnabled(application: Application):Boolean {
       val locationManager = application.getSystemService(
           Context.LOCATION_SERVICE
       ) as LocationManager
       return locationManager
           .isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
               locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
   }
}