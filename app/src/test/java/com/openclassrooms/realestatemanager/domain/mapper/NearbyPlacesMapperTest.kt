package com.openclassrooms.realestatemanager.domain.mapper

import com.openclassrooms.realestatemanager.data.local.model.PropertyNearbyPlaces
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import junit.framework.TestCase.assertEquals
import org.junit.Test

class NearbyPlacesMapperTest{
    private val nearbyPlacesMapper = NearbyPlacesMapper()

    @Test
    fun `mapToRoomEntities should map List of NearbyPlacesType to List of PropertyNearbyPlaces`() {
        val propertyId = 1L
        val nearbyPlaces = listOf(
            NearbyPlacesType.PARC,
            NearbyPlacesType.PHARMACY,
            NearbyPlacesType.SCHOOL
        )

        val result = nearbyPlacesMapper.mapToRoomEntities(nearbyPlaces, propertyId)

        assertEquals(nearbyPlaces.size, result.size)
        for (i in nearbyPlaces.indices) {
            assertEquals(propertyId, result[i].propertyId)
            assertEquals(nearbyPlaces[i], result[i].nearbyType)
        }
    }

    @Test
    fun `mapToNearbyPlacesList should map List of PropertyNearbyPlaces to List of NearbyPlacesType`() {
        val nearbyPlaces = listOf(
            PropertyNearbyPlaces(propertyId = 1, nearbyType = NearbyPlacesType.PARC),
            PropertyNearbyPlaces(propertyId = 1, nearbyType = NearbyPlacesType.PHARMACY),
            PropertyNearbyPlaces(propertyId = 1, nearbyType =  NearbyPlacesType.SCHOOL)
        )

        val result = nearbyPlacesMapper.mapToNearbyPlacesList(nearbyPlaces)

        assertEquals(nearbyPlaces.size, result?.size)
        result?.forEachIndexed { i, nearbyPlaceType ->
            assertEquals(nearbyPlaces[i].nearbyType, nearbyPlaceType)
        }
    }

}