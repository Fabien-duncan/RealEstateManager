package com.openclassrooms.realestatemanager.presentation.create_edit

import android.net.Uri
import com.openclassrooms.realestatemanager.MainCoroutineRule
import com.openclassrooms.realestatemanager.ProvideTestProperties
import com.openclassrooms.realestatemanager.domain.Connection.ConnectionCheckerRepository
import com.openclassrooms.realestatemanager.domain.geocoding.LatLongEntity
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.use_cases.AddPropertyUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.CheckInternetConnectionUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetCurrencyUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetLatLngFromAddressUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetPropertyByIdUseCase
import com.openclassrooms.realestatemanager.enums.CurrencyType
import com.openclassrooms.realestatemanager.enums.PropertyType
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date


class AddEditViewModelTest{
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
    @MockK
    lateinit var addPropertyUseCase: AddPropertyUseCase
    @MockK
    lateinit var getPropertyByIdUseCase: GetPropertyByIdUseCase
    @MockK
    lateinit var getLatLngFromAddressUseCase: GetLatLngFromAddressUseCase
    @MockK
    lateinit var getCurrencyUseCase: GetCurrencyUseCase

    lateinit var viewModel: AddEditViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = AddEditViewModel(addPropertyUseCase,getPropertyByIdUseCase,getLatLngFromAddressUseCase,getCurrencyUseCase)
    }

    @Test
    fun `resetState should reset the state properties`() {
        viewModel.onAreaChange("123")
        viewModel.onAgentNameChange("TestAgent")

        viewModel.resetState()

        val state = viewModel.state
        assertEquals(AddEditState(), state)
    }
    @Test
    fun `onTypeChange should update propertyType in state`() {
        val propertyType = PropertyType.APARTMENT

        viewModel.onTypeChange(propertyType)

        val state = viewModel.state
        assertEquals(propertyType, state.type)
    }
    @Test
    fun `onPriceChange should update price in state`() {
        val propertyPrice = "10000"

        viewModel.onPriceChange(propertyPrice)

        val state = viewModel.state
        assertEquals(propertyPrice, state.price.toString())
    }
    @Test
    fun `onAreaChange should update area in state`() {
        val propertyArea = "123"

        viewModel.onAreaChange(propertyArea)

        val state = viewModel.state
        assertEquals(propertyArea, state.area.toString())
    }
    @Test
    fun `onRoomsChange should update rooms in state`() {
        val rooms = "5"

        viewModel.onRoomsChange(rooms)

        val state = viewModel.state
        assertEquals(rooms, state.rooms.toString())
    }
    @Test
    fun `onBedroomsChange should update bedrooms in state`() {
        val bedrooms = "2"

        viewModel.onBedroomsChange(bedrooms)

        val state = viewModel.state
        assertEquals(bedrooms, state.bedrooms.toString())
    }
    @Test
    fun `onBathroomsChange should update Bathrooms in state`() {
        val bathrooms = "2"

        viewModel.onBathroomsChange(bathrooms)

        val state = viewModel.state
        assertEquals(bathrooms, state.bathrooms.toString())
    }
    @Test
    fun `onDescriptionChange should update description in state`() {
        val description = "test description"

        viewModel.onDescriptionChange(description)

        val state = viewModel.state
        assertEquals(description, state.description)
    }
    @Test
    fun `onIsSoldChange should update isSold in state`() {
        val startingState = viewModel.state
        viewModel.onIsSoldChange()

        val state = viewModel.state
        assertEquals(!startingState.isSold, state.isSold)
    }
    @Test
    fun `onSoldDateChange should update soldDate in state`() {
        val soldDate = Date()

        viewModel.onSoldDateChange(soldDate)

        val state = viewModel.state
        assertEquals(soldDate, state.soldDate)
    }
    @Test
    fun `onAgentNameChange should update agentName in state`() {
        val agentName = "test Agent"

        viewModel.onAgentNameChange(agentName)

        val state = viewModel.state
        assertEquals(agentName, state.agentName)
    }
    @Test
    fun `onNumberChange should update number in state`() {
        val number = "111"

        viewModel.onNumberChange(number)

        val state = viewModel.state
        assertEquals(number, state.number.toString())
    }
    @Test
    fun `onStreetChange should update street in state`() {
        val street = "test street"

        viewModel.onStreetChange(street)

        val state = viewModel.state
        assertEquals(street, state.street.toString())
    }
    @Test
    fun `onExtraChange should update extra in state`() {
        val extra = "B"

        viewModel.onExtraChange(extra)

        val state = viewModel.state
        assertEquals(extra, state.extra.toString())
    }
    @Test
    fun `onCityChange should update city in state`() {
        val city = "test city"

        viewModel.onCityChange(city)

        val state = viewModel.state
        assertEquals(city, state.city.toString())
    }
    @Test
    fun `onStateChange should update state in state`() {
        val propertyState = "test state"

        viewModel.onStateChange(propertyState)

        val state = viewModel.state
        assertEquals(propertyState, state.state.toString())
    }
    @Test
    fun `onCountryChange should country state in state`() {
        val country = "test country"

        viewModel.onCountryChange(country)

        val state = viewModel.state
        assertEquals(country, state.country.toString())
    }
    @Test
    fun `onPostalCodeChange should postalCode state in state`() {
        val postalCode = "12345SE"

        viewModel.onPostalCodeChange(postalCode)

        val state = viewModel.state
        assertEquals(postalCode, state.postalCode.toString())
    }
    /*@Test
    fun `onPhotoCaptionChanged should update state and form validity`() {
        val listOfUri = mockk<List<Uri>>()
        viewModel.onImagesAdded()

        viewModel.onPhotoCaptionChanged("New Caption", 0)


        val currentState = viewModel.state
        assertEquals("New Caption", currentState.photos?.get(0)?.caption)
    }*/

    @Test
    fun `addOrUpdateProperty should update state and _isAddOrUpdatePropertyFinished`() {
        val propertyId = 1L
        val currencyType = CurrencyType.Dollar
        coEvery { addPropertyUseCase.invoke(any()) } returns propertyId
        coEvery { getCurrencyUseCase.invoke() } returns currencyType

        setAllRequiredFields()

        viewModel.addOrUpdateProperty()

        coVerify { addPropertyUseCase.invoke(any()) }
    }
    @Test
    fun `getLatLongFromAddress should update position and mapImageLink`() = runBlocking {
        val lat = 12.345
        val lon = 67.890
        val latLongEntity = LatLongEntity(lat, lon)
        coEvery { getLatLngFromAddressUseCase.invoke(any(),any()) } returns latLongEntity

        viewModel.getLatLongFromAddress()

        assertEquals(latLongEntity, viewModel.position)
        coVerify { getLatLngFromAddressUseCase.invoke(any(),any()) }
    }

    @Test
    fun `getPropertyById should update state and isAddressValid`() = runBlocking {
        val propertyId = 1L
        val currencyType = CurrencyType.Dollar
        val mockProperty = ProvideTestProperties.getTestProperty()
        coEvery { getPropertyByIdUseCase(propertyId) } returns flowOf(mockProperty)

        viewModel.getPropertyById(propertyId, currencyType)

        coVerify { getPropertyByIdUseCase(propertyId) }
        assertEquals(mockProperty.id, viewModel.state.id)
        assertEquals(mockProperty.price, viewModel.state.price)

        assertEquals(mockProperty.address.latitude != null && mockProperty.address.longitude != null, viewModel.isAddressValid)
    }
    @Test
    fun `resetFinishedState should set isAddOrUpdatePropertyFinished to false`(){
            viewModel.resetFinishedState()

            // Then
            assertFalse(viewModel.isAddOrUpdatePropertyFinished)
        }


    private fun setAllRequiredFields(){
        viewModel.onTypeChange(PropertyType.CONDO)
        viewModel.onPriceChange("10000")
        viewModel.onAreaChange("123")
        viewModel.onRoomsChange("5")
        viewModel.onBedroomsChange("2")
        viewModel.onBathroomsChange("2")
        viewModel.onDescriptionChange("A nice house!")
        viewModel.onIsSoldChange()
        viewModel.onSoldDateChange(Date())
        viewModel.onAgentNameChange("test Agent")
        viewModel.onNumberChange("111")
        viewModel.onStreetChange("test street")
        viewModel.onExtraChange("A")
        viewModel.onCityChange("test city")
        viewModel.onStateChange("test state")
        viewModel.onCountryChange("test country")
        viewModel.onPostalCodeChange("1234SE")
    }

}