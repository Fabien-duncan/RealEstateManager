package com.openclassrooms.realestatemanager.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Property::class,
            parentColumns = ["id"],
            childColumns = ["propertyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["propertyId"])]
)
data class PropertyAddress(
    @PrimaryKey(autoGenerate = true)
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
