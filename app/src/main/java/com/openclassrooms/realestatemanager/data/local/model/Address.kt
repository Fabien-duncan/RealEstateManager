package com.openclassrooms.realestatemanager.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Address(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "property_id", index = true)
    val propertyId: Long,
    val street: String,
    val city: String,
    val state: String,
    val country: String,
    @ColumnInfo(name = "postal_code")
    val postalCode: String,
    val latitude: Double?=null,
    val longitude: Double?=null
)
