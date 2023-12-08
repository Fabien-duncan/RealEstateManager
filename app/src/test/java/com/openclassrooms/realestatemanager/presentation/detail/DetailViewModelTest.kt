package com.openclassrooms.realestatemanager.presentation.detail

import com.openclassrooms.realestatemanager.MainCoroutineRule
import com.openclassrooms.realestatemanager.ProvideTestProperties
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.use_cases.CheckInternetConnectionUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetAllPropertiesUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetCurrencyUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetCurrentLocationUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetFilteredPropertiesUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetPropertyByIdUseCase
import com.openclassrooms.realestatemanager.presentation.home.HomeViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest{
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
    @MockK
    private lateinit var getPropertyByIdUseCase: GetPropertyByIdUseCase

    @MockK
    private lateinit var checkInternetConnectionUseCase: CheckInternetConnectionUseCase

    private lateinit var viewModel: DetailViewModel

    private lateinit var property:PropertyModel
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        property = ProvideTestProperties.getTestProperty()

    }
    @Test
    fun `updatePropertyById should update state and mapImageLink on success`() = runBlocking {
        coEvery { getPropertyByIdUseCase.invoke(any()) } returns flowOf(property)
        coEvery { checkInternetConnectionUseCase.invoke() } returns true
        viewModel = DetailViewModel(getPropertyByIdUseCase,checkInternetConnectionUseCase, 1L)

        viewModel.updatePropertyById(1L)

        assertEquals(property, viewModel.state.property)
        assertNotNull( viewModel.mapImageLink)
    }
    @Test
    fun `updatePropertyById should update state and set mapImageLink to 'no internet' on failure`() = runBlocking {
        coEvery { getPropertyByIdUseCase.invoke(any()) } returns flowOf(property)
        coEvery { checkInternetConnectionUseCase.invoke() } returns false
        viewModel = DetailViewModel(getPropertyByIdUseCase,checkInternetConnectionUseCase, 1L)

        viewModel.updatePropertyById(1L)

        assertEquals(DetailState(property), viewModel.state)
        assertEquals("no internet", viewModel.mapImageLink)
    }
}