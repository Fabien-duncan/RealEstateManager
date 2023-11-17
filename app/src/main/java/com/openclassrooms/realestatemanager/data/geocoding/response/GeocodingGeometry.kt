package com.openclassrooms.realestatemanager.data.geocoding.response

import com.google.gson.annotations.SerializedName

data class GeocodingGeometry(
    @SerializedName("location") val location: GeocodingLocation,
)
