package com.openclassrooms.realestatemanager.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.enums.PropertyType
import java.util.Date

@Entity
data class Property(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val type:PropertyType,
    val price:Double,
    val numRooms:Int,
    val description:String,
    val isSold:Boolean,
    val createdDate: Date,
    val soldData: Date?,
    val agentName: String
)
