package com.openclassrooms.realestatemanager.data.geocoding.response

import com.google.gson.annotations.SerializedName

data class GeocodingResult(
    @SerializedName("geometry") val geometry: GeocodingGeometry,
)
