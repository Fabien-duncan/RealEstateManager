package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.ProvideTestModelsOrRoomEntities
import com.openclassrooms.realestatemanager.domain.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test



class AddPropertyUseCaseTest {
    @MockK
    lateinit var mockRepository: Repository

    private lateinit var addPropertyUseCase: AddPropertyUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        addPropertyUseCase = AddPropertyUseCase(mockRepository)
    }
    @Test
    fun `invoke should call insert on the repository`() = runBlocking {
        val property = ProvideTestModelsOrRoomEntities.getTestPropertyModel()
        coEvery { addPropertyUseCase.invoke(property) } returns  property.id

        val id = addPropertyUseCase.invoke(property)

        coVerify { mockRepository.insert(property) }
        assertEquals(property.id, id)
    }
}