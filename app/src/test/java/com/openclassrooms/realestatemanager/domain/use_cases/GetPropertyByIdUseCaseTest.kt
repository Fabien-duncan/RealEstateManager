package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.ProvideTestModelsOrRoomEntities
import com.openclassrooms.realestatemanager.domain.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetPropertyByIdUseCaseTest{
    @MockK
    lateinit var mockRepository: Repository

    lateinit var getPropertyByIdUseCase: GetPropertyByIdUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getPropertyByIdUseCase = GetPropertyByIdUseCase(mockRepository)
    }

    @Test
    fun `invoke should return property by ID`() = runBlocking {
        val mockProperty = ProvideTestModelsOrRoomEntities.getTestPropertyModel()
        coEvery {
            mockRepository.getPropertyWithDetailsById(
                propertyId = any()
            )
        } returns flowOf(mockProperty)

        val resultFlow = getPropertyByIdUseCase(
            id = 1L
        )

        coVerify { mockRepository.getPropertyWithDetailsById(any()) }
        assertEquals(mockProperty, resultFlow.single())
    }
}