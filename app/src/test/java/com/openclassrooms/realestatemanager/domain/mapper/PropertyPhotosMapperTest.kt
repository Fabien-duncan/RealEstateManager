package com.openclassrooms.realestatemanager.domain.mapper

import com.openclassrooms.realestatemanager.ProvideTestModelsOrRoomEntities
import junit.framework.TestCase.assertEquals
import org.junit.Test


class PropertyPhotosMapperTest{
    private val propertyPhotosMapper = PropertyPhotosMapper()

    @Test
    fun `mapToRoomEntities should map List of PropertyPhotosModel to List of PropertyPhotos`() {
        val propertyPhotosModelList = ProvideTestModelsOrRoomEntities.getTestPhotosModel()
        val propertyId = 1L

        val result = propertyPhotosMapper.mapToRoomEntities(propertyPhotosModelList, propertyId)

        assertEquals(propertyPhotosModelList.size, result.size)
        assertEquals(propertyPhotosModelList[0].photoPath, result[0].photoPath)
    }

    @Test
    fun `mapToListOfDomainModels should map List of PropertyPhotos to List of PropertyPhotosModel`() {
        val propertyPhotosList = ProvideTestModelsOrRoomEntities.getTestPhotosRoomEntity()

        val result = propertyPhotosMapper.mapToListOfDomainModels(propertyPhotosList)

        assertEquals(propertyPhotosList.size, result.size)
        assertEquals(propertyPhotosList[1].photoPath, result[1].photoPath)
    }
}