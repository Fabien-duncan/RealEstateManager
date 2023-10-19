package com.openclassrooms.realestatemanager.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.enums.PropertyType
import java.util.Date

@Entity(
    indices = [Index(value = ["id"], unique = true)]
)
data class Property(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val type:PropertyType,
    val price:Double,
    val area:Int,
    val rooms: Int,
    val bedrooms: Int,
    val bathrooms: Int,
    val description:String,
    @ColumnInfo(name = "is_sold")
    val isSold:Boolean = false,
    @ColumnInfo(name = "created_date")
    val createdDate: Date = Date(),
    @ColumnInfo(name = "sold_date")
    val soldDate: Date? = null,
    @ColumnInfo(name = "agent_name")
    val agentName: String
)
