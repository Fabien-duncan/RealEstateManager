package com.openclassrooms.realestatemanager.framework

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.data.local.RealEstateDataBase

class RealEstateContentProvider: ContentProvider(){
    private lateinit var database: RealEstateDataBase

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    companion object {
        private const val AUTHORITY = "com.openclassrooms.realestatemanager.framework.RealEstateContentProvider"
        private const val PATH_PROPERTIES = "properties"
        private const val PATH_ADDRESS = "addresses"
        private const val PATH_NEARBY_PLACES = "property_nearby_places"
        private const val PATH_PHOTOS = "property_photos"

        private val URI_PROPERTIES = 1
        private val URI_ADDRESS = 2
        private val URI_NEARBY_PLACES = 3
        private val URI_PHOTOS = 4
        private val URI_PROPERTY_BY_ID = 5

    }
    init {
        uriMatcher.addURI(AUTHORITY, PATH_PROPERTIES, URI_PROPERTIES)
        uriMatcher.addURI(AUTHORITY, PATH_ADDRESS, URI_ADDRESS)
        uriMatcher.addURI(AUTHORITY, PATH_NEARBY_PLACES, URI_NEARBY_PLACES)
        uriMatcher.addURI(AUTHORITY, PATH_PHOTOS, URI_PHOTOS)
        uriMatcher.addURI(AUTHORITY, PATH_PROPERTIES, URI_PROPERTY_BY_ID)
    }
    override fun onCreate(): Boolean {
        database = RealEstateDataBase.getInstance(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor: Cursor? = when (uriMatcher.match(uri)) {
            URI_PROPERTIES -> database.propertyDao.getAllPropertiesAsCursor()
            URI_ADDRESS -> database.propertyDao.getAllAddressesAsCursor()
            URI_NEARBY_PLACES -> database.propertyDao.getAllNearbyPlacesAsCursor()
            URI_PHOTOS -> database.propertyDao.getAllPhotosAsCursor()
            URI_PROPERTY_BY_ID -> {
                // Extract the property ID from the URI
                val propertyId = ContentUris.parseId(uri)
                database.propertyDao.getPropertyByIdAsCursor(propertyId)
            }
            else -> null
        }

        cursor?.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun getType(p0: Uri): String? {
        return when (uriMatcher.match(p0)) {
            URI_PROPERTIES -> "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_PROPERTIES"
            URI_ADDRESS -> "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_ADDRESS"
            URI_NEARBY_PLACES -> "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_NEARBY_PLACES"
            URI_PHOTOS -> "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_PHOTOS"
            else -> null
        }
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }
}