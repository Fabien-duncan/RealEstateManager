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
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel
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
                val testProperties = getAllPropertiesWithAllDetails()
                testProperties.forEach{ property ->
                    db.propertyDao.insert(property.property)
                    db.propertyDao.insert(property.address)
                    property.photos?.let {
                            db.propertyDao.insert(it)
                    }
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

        private fun getAllPhotos(): List<PropertyPhotos>{
            return listOf(
                PropertyPhotos(propertyId = 2, photoPath = "https://images.pexels.com/photos/268533/pexels-photo-268533.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", caption = "view"),
                PropertyPhotos(propertyId = 2, photoPath = "https://www.mydomaine.com/thmb/CaWdFGvTH4-h1VvG6tukpKuU2lM=/3409x0/filters:no_upscale():strip_icc()/binary-4--583f06853df78c6f6a9e0b7a.jpeg", caption = "Facade"),
                PropertyPhotos(propertyId = 2, photoPath = "https://designingidea.com/wp-content/uploads/2022/01/modern-home-types-of-room-living-space-dining-area-kitchen-glass-door-floor-rug-loft-pendant-light-ss.jpg", caption = null),
                PropertyPhotos(propertyId = 2, photoPath = "https://foyr.com/learn/wp-content/uploads/2022/05/foyer-or-entry-hall-types-of-rooms-in-a-house-1024x819.jpg", caption = "entrance"),

                PropertyPhotos(propertyId = 1, photoPath = "https://www.indexsante.ca/chroniques/images/appartement-residence.jpg", caption = "facade"),
                PropertyPhotos(propertyId = 1, photoPath = "https://media.lesechos.com/api/v1/images/view/634e807253fc0b2c225abf6a/1280x720/0702580327940-web-tete.jpg", caption = "lounge"),
                PropertyPhotos(propertyId = 1, photoPath = "https://www.bhg.com/thmb/MaQDVndcD-FF3qtf9e50rmfVml4=/4000x0/filters:no_upscale():strip_icc()/bhg-modern-kitchen-8RbSHoA8aKT9tEG-DcYr56-039892da05774ea78f8682b3f693bb5d.jpg", caption = "kitchen"),

                PropertyPhotos(propertyId = 1, photoPath = "https://foyr.com/learn/wp-content/uploads/2022/05/foyer-or-entry-hall-types-of-rooms-in-a-house-1024x819.jpg", caption = "entrance")

            )

        }

        private fun getAllPropertiesWithAllDetails():List<PropertyWithAllDetails>{
            val testProperties = getAllProperties()
            val testAddress = getAllAddresses()
            val testPhotos = getAllPhotos()

            return listOf(
                PropertyWithAllDetails(property = testProperties[0], address = testAddress[0],photos = testPhotos.filter { it.propertyId == 1.toLong() }, nearbyPlaces = null),
                PropertyWithAllDetails(property = testProperties[1], address = testAddress[1],photos = testPhotos.filter { it.propertyId == 2.toLong() }, nearbyPlaces = null)
            )
        }
    }
}
