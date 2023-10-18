package com.openclassrooms.realestatemanager.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.enums.PropertyType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DatabaseCallBack @Inject constructor(
    private val propertyDao: PropertyDao
) : RoomDatabase.Callback(){
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        GlobalScope.launch {
            val tesProperties = getAllProperties()
            tesProperties.forEach{ property ->
                propertyDao.insert(property)
            }
        }
    }
}

private fun getAllProperties(): List<Property>{
    return listOf(
        Property(
            id = 1,
            addressId = 1,
            type = PropertyType.APARTMENT,
            price = 152046.50,
            surfaceArea = 56,
            numRooms = 3,
            description = "very nice house",
            agentName = "Fabien Duncan"
        ),
        Property(
            id = 2,
            addressId = 2,
            type = PropertyType.MANOIR,
            price = 305046.50,
            surfaceArea = 102,
            numRooms = 5,
            description = "near a very nice school",
            agentName = "Fabien Duncan"
        ),
        Property(
            id = 3,
            addressId = 3,
            type = PropertyType.LOFT,
            price = 485046.50,
            surfaceArea = 35,
            numRooms = 2,
            description = "clean house",
            agentName = "Fabien Duncan"
        ),
        Property(
            id = 4,
            addressId = 4,
            type = PropertyType.APARTMENT,
            price = 85433.99,
            surfaceArea = 27,
            numRooms = 2,
            description = "Nice a cosy",
            agentName = "Fabien Duncan"
        ),
    )
}