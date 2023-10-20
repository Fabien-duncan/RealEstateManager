package com.openclassrooms.realestatemanager.domain.model

data class AddressModel(
    val id: Long = 0,
    val propertyId: Long,
    val street: String,
    val city: String,
    val state: String,
    val country: String,
    val postalCode: String,
    val latitude: Double?,
    val longitude: Double?
)