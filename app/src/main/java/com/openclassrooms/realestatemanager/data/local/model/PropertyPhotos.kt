package com.openclassrooms.realestatemanager.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity
data class PropertyPhotos(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "property_id", index = true)
    val propertyId: Long,
    val photoPath: String,
    val caption: String?

)
