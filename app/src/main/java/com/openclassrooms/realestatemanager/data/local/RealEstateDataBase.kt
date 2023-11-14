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
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
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
                    property.nearbyPlaces?.let {
                        db.propertyDao.insertNearbyPlaces(it)
                    }
                }
            }
        }

        private fun getAllProperties(): List<Property> {
            return listOf(
                Property(
                    type = PropertyType.LOFT,
                    price = 1435577,
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
                    price = 877502,
                    area = 206,
                    rooms = 8,
                    bedrooms = 5,
                    bathrooms = 2,
                    description = "Mauris ullamcorper purus sit amet nulla. Quisque arcu libero, rutrum ac, lobortis vel, dapibus at, diam. Nam tristique tortor eu pede.",
                    isSold = false,
                    agentName = "John Do"
                ),
                Property(
                    type = PropertyType.APARTMENT,
                    price = 482515,
                    area = 227,
                    rooms = 2,
                    bedrooms = 1,
                    bathrooms = 1,
                    description = "Morbi quis tortor id nulla ultrices aliquet. Maecenas leo odio, condimentum id, luctus nec, molestie sed, justo. Pellentesque viverra pede ac diam. Cras pellentesque volutpat dui. Maecenas tristique, est et tempus semper, est quam pharetra magna, ac consequat metus sapien ut nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Mauris viverra diam vitae quam. Suspendisse potenti.",
                    isSold = false,
                    agentName = "Fabien Duncan"
                ),
                Property(
                    type = PropertyType.CONDO,
                    price = 4078847,
                    area = 547,
                    rooms = 15,
                    bedrooms = 8,
                    bathrooms = 7,
                    description = "Duis consequat dui nec nisi volutpat eleifend. Donec ut dolor. Morbi vel lectus in quam fringilla rhoncus. Mauris enim leo, rhoncus sed, vestibulum sit amet, cursus id, turpis.",
                    isSold = false,
                    agentName = "Fabien Duncan"
                )
            )
        }
        private fun getAllAddresses(): List<Address> {
            return listOf(
                Address(propertyId = 1, number = 73486, street = "Sachs Center", extra = "apt 7B", city = "Jeffersonville", state = "Indiana", country = "United States", postalCode = "47134"),
                Address(propertyId = 2, number = 1,street = "Mallard Court", city = "Sacramento", state = "California", country = "United States", postalCode = "95828"),
                Address(propertyId = 3, number = 26, street = "Clove Hill", extra = "231", city = "San Jose", state = "California", country = "United States", postalCode = "95194"),
                Address(propertyId = 4,number = 9943, street = "Warbler Circle", city = "Richmond", state = "Virginia", country = "United States", postalCode = "23220"),
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
                PropertyPhotos(propertyId = 1, photoPath = "https://www.ikea.com/ext/ingkadam/m/261c243e95fe8c7d/original/PH195797.jpg?f=s", caption = "kitchen"),

                PropertyPhotos(propertyId = 4, photoPath = "https://www.edinarealty.com/media/3678/difference-between-condo.jpg?mode=crop&width=800&height=540", caption = "facade")

            )
        }

        private fun getAllPropertyNearbyPlaces():List<PropertyNearbyPlaces>{
            return listOf(
                PropertyNearbyPlaces(propertyId = 1, nearbyType = NearbyPlacesType.PLAYGROUND),
                PropertyNearbyPlaces(propertyId = 1, nearbyType = NearbyPlacesType.SCHOOL),
                PropertyNearbyPlaces(propertyId = 1, nearbyType = NearbyPlacesType.HOSPITAL),
                PropertyNearbyPlaces(propertyId = 1, nearbyType = NearbyPlacesType.NATIONAL_PARC),
                PropertyNearbyPlaces(propertyId = 1, nearbyType = NearbyPlacesType.PHARMACY),

                PropertyNearbyPlaces(propertyId = 2, nearbyType = NearbyPlacesType.SUPERMARKET),
                PropertyNearbyPlaces(propertyId = 2, nearbyType = NearbyPlacesType.PARC),
                PropertyNearbyPlaces(propertyId = 2, nearbyType = NearbyPlacesType.PHARMACY),

                PropertyNearbyPlaces(propertyId = 3, nearbyType = NearbyPlacesType.PHARMACY),
                PropertyNearbyPlaces(propertyId = 3, nearbyType = NearbyPlacesType.PARC),
                PropertyNearbyPlaces(propertyId = 3, nearbyType = NearbyPlacesType.PLAYGROUND),
                PropertyNearbyPlaces(propertyId = 3, nearbyType = NearbyPlacesType.SCHOOL),
                PropertyNearbyPlaces(propertyId = 3, nearbyType = NearbyPlacesType.SUPERMARKET),
                PropertyNearbyPlaces(propertyId = 3, nearbyType = NearbyPlacesType.NATIONAL_PARC),
                PropertyNearbyPlaces(propertyId = 3, nearbyType = NearbyPlacesType.HOSPITAL),
                PropertyNearbyPlaces(propertyId = 3, nearbyType = NearbyPlacesType.BEACH),
                PropertyNearbyPlaces(propertyId = 3, nearbyType = NearbyPlacesType.LIBRARY),
                PropertyNearbyPlaces(propertyId = 3, nearbyType = NearbyPlacesType.SHOP),
            )

        }

        private fun getAllPropertiesWithAllDetails():List<PropertyWithAllDetails>{
            val testProperties = getAllProperties()
            val testAddress = getAllAddresses()
            val testPhotos = getAllPhotos()
            val testNearbyPlaces = getAllPropertyNearbyPlaces()

            return listOf(
                PropertyWithAllDetails(property = testProperties[0], address = testAddress[0],photos = testPhotos.filter { it.propertyId == 1.toLong() }, nearbyPlaces = testNearbyPlaces.filter { it.propertyId == 1.toLong() }),
                PropertyWithAllDetails(property = testProperties[1], address = testAddress[1],photos = testPhotos.filter { it.propertyId == 2.toLong() }, nearbyPlaces = testNearbyPlaces.filter { it.propertyId == 2.toLong() }),
                PropertyWithAllDetails(property = testProperties[2], address = testAddress[2],photos = null, nearbyPlaces = testNearbyPlaces.filter { it.propertyId == 3.toLong()}),
                PropertyWithAllDetails(property = testProperties[3], address = testAddress[3],photos = testPhotos.filter { it.propertyId == 4.toLong() }, nearbyPlaces = null)
            )
        }
    }
}
