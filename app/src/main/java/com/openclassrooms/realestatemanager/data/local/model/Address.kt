package com.openclassrooms.realestatemanager.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addresses")
data class Address(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "property_id", index = true)
    val propertyId: Long,
    val number: Int,
    val street: String,
    val extra: String? = null, //flat number or any extra detail
    val city: String,
    val state: String,
    val country: String,
    @ColumnInfo(name = "postal_code")
    val postalCode: String,
    val latitude: Double?=null,
    val longitude: Double?=null
)
