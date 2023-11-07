package com.openclassrooms.realestatemanager.domain.model

data class AddressModel(
    val id: Long = 0,
    val propertyId: Long,
    val number:Int,
    val street: String,
    val extra: String?, //flat number or any extra detail
    val city: String,
    val state: String,
    val country: String,
    val postalCode: String,
    val latitude: Double?,
    val longitude: Double?
)