package com.openclassrooms.realestatemanager.presentation.home

import android.location.Location
import com.openclassrooms.realestatemanager.MainCoroutineRule
import com.openclassrooms.realestatemanager.ProvideTestModelsOrRoomEntities
import com.openclassrooms.realestatemanager.common.ScreenViewState
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.use_cases.GetAllPropertiesUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetCurrencyUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetCurrentLocationUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetFilteredPropertiesUseCase
import com.openclassrooms.realestatemanager.enums.CurrencyType
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import com.openclassrooms.realestatemanager.presentation.navigation.FilterState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.internal.ignoreIoExceptions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
    @MockK
    private lateinit var getAllPropertiesUseCase: GetAllPropertiesUseCase

    @MockK
    private lateinit var getFilteredPropertiesUseCase: GetFilteredPropertiesUseCase

    @MockK
    private lateinit var getCurrentLocationUseCase: GetCurrentLocationUseCase

    @MockK
    private lateinit var getCurrencyUseCase: GetCurrencyUseCase

    private lateinit var viewModel: HomeViewModel

    private  lateinit var properties:List<PropertyModel>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        properties = ProvideTestModelsOrRoomEntities.getTestProperties()

    }

    @Test
    fun `getAllProperty should update state on success`() = runBlocking {
        coEvery { getAllPropertiesUseCase.invoke() } returns flowOf(properties)
        viewModel = HomeViewModel(
            getAllPropertiesUseCase,
            getFilteredPropertiesUseCase,
            getCurrentLocationUseCase,
            getCurrencyUseCase
        )

        viewModel.getAllProperty()

        val currentState = viewModel.state.value
        assertEquals(ScreenViewState.Success(properties), currentState.properties)
        assertEquals(properties[0].id, viewModel.currentId)
        assertEquals(0, viewModel.propertyIndex)
    }

    @Test(expected = Exception::class)
    fun `getAllProperty should update state on error`() = runBlocking{
        val errorMessage = "An error occurred"
        coEvery { getAllPropertiesUseCase() } throws Exception(errorMessage)
        viewModel = HomeViewModel(
            getAllPropertiesUseCase,
            getFilteredPropertiesUseCase,
            getCurrentLocationUseCase,
            getCurrencyUseCase
        )

        viewModel.getAllProperty()

        coVerify { getAllPropertiesUseCase() }
        val currentState = viewModel.state.value
        assertEquals(ScreenViewState.Error(errorMessage), currentState.properties)
    }

    @Test
    fun `getCurrentLocation should update currentLocation on success`() = runBlocking {
        coEvery { getAllPropertiesUseCase.invoke() } returns flowOf(properties)
        viewModel = HomeViewModel(
            getAllPropertiesUseCase,
            getFilteredPropertiesUseCase,
            getCurrentLocationUseCase,
            getCurrencyUseCase
        )
        val expectedLocation = mockk<Location>()
        coEvery { getCurrentLocationUseCase.invoke() } returns expectedLocation

        viewModel.getCurrentLocation()

        assertEquals(expectedLocation, viewModel.currentLocation)
    }

    @Test()
    fun `getCurrentLocation should not update currentLocation on failure`() = runBlocking {
        coEvery { getAllPropertiesUseCase.invoke() } returns flowOf(properties)
        viewModel = HomeViewModel(
            getAllPropertiesUseCase,
            getFilteredPropertiesUseCase,
            getCurrentLocationUseCase,
            getCurrencyUseCase
        )
        coEvery { getCurrentLocationUseCase.invoke() } throws Exception("Failed to get location")


        viewModel.getCurrentLocation()

        assertEquals(null, viewModel.currentLocation)
    }
    @Test
    fun `getFilteredProperties should update state on success`() = runBlocking{
        coEvery { getAllPropertiesUseCase.invoke() } returns flowOf(properties)
        viewModel = HomeViewModel(
            getAllPropertiesUseCase,
            getFilteredPropertiesUseCase,
            getCurrentLocationUseCase,
            getCurrencyUseCase
        )
        coEvery { getCurrencyUseCase.invoke() } returns CurrencyType.Dollar

        val nearbyPlaces = listOf( NearbyPlacesType.PARC, NearbyPlacesType.SHOP)
        val filterState = FilterState(minPrice = 100, maxPrice = 200, nearbyPlaces = nearbyPlaces)

        coEvery { getFilteredPropertiesUseCase.invoke(any(), any(), any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any() ) } returns flowOf(properties)


       viewModel.getFilteredProperties(filterState)

        val currentState = viewModel.state.value
        coVerify { getFilteredPropertiesUseCase.invoke(any(), any(), any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any() ) }
        assertEquals(ScreenViewState.Success(properties), currentState.properties)
        assertEquals(properties[0].id, viewModel.currentId)
        assertEquals(0, viewModel.propertyIndex)
    }
    @Test(expected = Exception::class)
    fun `getFilteredProperties should update state on failure`() = runBlocking{
        coEvery { getAllPropertiesUseCase.invoke() } returns flowOf(properties)
        viewModel = HomeViewModel(
            getAllPropertiesUseCase,
            getFilteredPropertiesUseCase,
            getCurrentLocationUseCase,
            getCurrencyUseCase
        )
        coEvery { getCurrencyUseCase.invoke() } returns CurrencyType.Dollar
        val nearbyPlaces = listOf( NearbyPlacesType.PARC, NearbyPlacesType.SHOP)
        val filterState = FilterState(minPrice = 100, maxPrice = 200, nearbyPlaces = nearbyPlaces)

        coEvery { getFilteredPropertiesUseCase.invoke(any(), any(), any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any() ) } throws Exception("Failed to filter properties")

        viewModel.getFilteredProperties(filterState)

        assertEquals(ScreenViewState.Error("Failed to filter properties"), viewModel.state.value.properties)
    }

}