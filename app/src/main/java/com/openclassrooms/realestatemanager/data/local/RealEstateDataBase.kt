package com.openclassrooms.realestatemanager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.data.local.converters.DateConverter
import com.openclassrooms.realestatemanager.data.local.converters.NearbyPlacesTypeConverter
import com.openclassrooms.realestatemanager.data.local.converters.PropertyTypeConverter
import com.openclassrooms.realestatemanager.data.local.model.Property
import com.openclassrooms.realestatemanager.data.local.model.Address
import com.openclassrooms.realestatemanager.data.local.model.PropertyNearbyPlaces
import com.openclassrooms.realestatemanager.data.local.model.PropertyPhotos
import com.openclassrooms.realestatemanager.data.local.model.PropertyWithAllDetails
import com.openclassrooms.realestatemanager.enums.PropertyType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date

@TypeConverters(NearbyPlacesTypeConverter::class, PropertyTypeConverter::class, DateConverter::class)
@Database(
    entities = [Property::class, Address::class, PropertyNearbyPlaces::class, PropertyPhotos::class],
    version = 1,
    exportSchema = false
)
abstract class RealEstateDataBase:RoomDatabase() {
    abstract val propertyDao: PropertyDao

    companion object {
        private var instance: RealEstateDataBase? = null

        @Synchronized
        fun getInstance(ctx: Context): RealEstateDataBase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    ctx.applicationContext, RealEstateDataBase::class.java,
                    "real_estate_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        private fun populateDatabase(db: RealEstateDataBase) {
            val noteDao = db.propertyDao
            GlobalScope.launch {
                val testProperties = getAllProperties()
                testProperties.forEach{ property ->
                    db.propertyDao.insert(property)
                }
                val testAddress = getAllAddresses()
                testAddress.forEach{ address ->
                    db.propertyDao.insert(address)
                }
            }
        }

        private fun getAllProperties(): List<Property> {
            return listOf(
                Property(
                    type = PropertyType.LOFT,
                    price = 1435577.38,
                    area = 118,
                    rooms = 3,
                    bedrooms = 2,
                    bathrooms = 1,
                    description = "Nullam sit amet turpis elementum ligula vehicula consequat. Morbi a ipsum. Integer a nibh. In quis justo. Maecenas rhoncus aliquam lacus. Morbi quis tortor id nulla ultrices aliquet.",
                    isSold = true,
                    soldDate = Date(),
                    agentName = "Marion Chenus"
                ),
                Property(
                    type = PropertyType.HOUSE,
                    price = 877502.05,
                    area = 206,
                    rooms = 8,
                    bedrooms = 5,
                    bathrooms = 2,
                    description = "Mauris ullamcorper purus sit amet nulla. Quisque arcu libero, rutrum ac, lobortis vel, dapibus at, diam. Nam tristique tortor eu pede.",
                    isSold = false,
                    soldDate = Date(),
                    agentName = "John Do"
                )
            )
        }
        private fun getAllAddresses(): List<Address> {
            return listOf(
                Address(propertyId = 1, street = "73486 Sachs Center", city = "Jeffersonville", state = "Indiana", country = "United States", postalCode = "47134"),
                Address(propertyId = 2, street = "1 Mallard Court", city = "Sacramento", state = "California", country = "United States", postalCode = "95828")

            )
        }
    }
}
