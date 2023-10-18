package com.openclassrooms.realestatemanager.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.enums.PropertyType
import java.util.Date

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = PropertyAddress::class,
            parentColumns = ["id"],
            childColumns = ["addressId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["addressId"])]
)
data class Property(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val addressId:Long,
    val type:PropertyType,
    val price:Double,
    val surfaceArea:Int,
    val numRooms:Int,
    val description:String,
    val isSold:Boolean = false,
    val createdDate: Date = Date(),
    val soldDate: Date? = null,
    val agentName: String
)
