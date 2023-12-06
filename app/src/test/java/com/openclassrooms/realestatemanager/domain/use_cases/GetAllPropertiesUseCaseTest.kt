package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.domain.model.AddressModel
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.repository.Repository
import com.openclassrooms.realestatemanager.enums.PropertyType
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetAllPropertiesUseCaseTest {

    @MockK
    lateinit var mockRepository: Repository

    lateinit var getAllPropertiesUseCase: GetAllPropertiesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getAllPropertiesUseCase = GetAllPropertiesUseCase(mockRepository)
    }
    @Test
    fun `invoke should return flow of properties`() = runBlocking {
        val mockPropertiesList = ProvideTestProperties.getTestProperties()
        coEvery { mockRepository.getAllPropertiesWithDetails() } returns flowOf(mockPropertiesList)

        val resultFlow = getAllPropertiesUseCase()

        coVerify { mockRepository.getAllPropertiesWithDetails() }
        TestCase.assertEquals(mockPropertiesList, resultFlow.single())
    }
}