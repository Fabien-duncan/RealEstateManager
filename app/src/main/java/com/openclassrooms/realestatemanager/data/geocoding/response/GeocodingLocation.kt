package com.openclassrooms.realestatemanager.data.geocoding.response

import com.google.gson.annotations.SerializedName

data class GeocodingLocation(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)
