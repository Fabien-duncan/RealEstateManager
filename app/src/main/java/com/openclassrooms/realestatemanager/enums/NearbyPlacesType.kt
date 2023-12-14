package com.openclassrooms.realestatemanager.enums

enum class NearbyPlacesType(private val textValue: String? = null) {
    SCHOOL,
    PARC,
    SUPERMARKET,
    SHOP,
    BEACH,
    NATIONAL_PARC("National park"),
    PLAYGROUND,
    HOSPITAL,
    LIBRARY,
    PHARMACY;

    val displayText: String
        get() = textValue ?: name
}