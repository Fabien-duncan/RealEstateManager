package com.openclassrooms.realestatemanager.presentation.create_edit

import android.content.Context
import android.net.Uri
import com.openclassrooms.realestatemanager.MainCoroutineRule
import com.openclassrooms.realestatemanager.ProvideTestModelsOrRoomEntities
import com.openclassrooms.realestatemanager.common.utils.FileUtils
import com.openclassrooms.realestatemanager.domain.geocoding.LatLongEntity
import com.openclassrooms.realestatemanager.domain.model.PropertyPhotosModel
import com.openclassrooms.realestatemanager.domain.use_cases.AddPropertyUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetCurrencyUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetLatLngFromAddressUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetPropertyByIdUseCase
import com.openclassrooms.realestatemanager.enums.CurrencyType
import com.openclassrooms.realestatemanager.enums.NearbyPlacesType
import com.openclassrooms.realestatemanager.enums.PropertyType
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
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
    @MockK
    lateinit var fileUtils: FileUtils

    private lateinit var viewModel: AddEditViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = AddEditViewModel(addPropertyUseCase,getPropertyByIdUseCase,getLatLngFromAddressUseCase,getCurrencyUseCase, fileUtils)
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
    @Test
    fun `onPhotoCaptionChanged should update state and form validity`() {
        val mockContext = mockk<Context>()
        val testUri  = mockk<Uri>(relaxed = true)
        val listOfTestUris = listOf(testUri)
        coEvery { testUri.toString() }returns "content://com.example.provider/data/test"
        coEvery { fileUtils.copyImageToInternalStorage(any(),any())} returns testUri
        viewModel.onImagesAdded(listOfTestUris, mockContext)

        viewModel.onPhotoCaptionChanged("New Caption", 0)

        val currentState = viewModel.state
        assertEquals("New Caption", currentState.photos?.get(0)?.caption)
    }

    @Test
    fun `onImagesAdded should add photos to the state`() {
        val uriTest1 = mockk<Uri>(relaxed = true)
        val uriTest2 = mockk<Uri>(relaxed = true)
        coEvery { uriTest1.toString() }returns "content://com.example.provider/image/1"
        coEvery { uriTest2.toString() }returns "content://com.example.provider/image/2"
        val originalImagesUris = listOf(uriTest1, uriTest2)
        val mockContext = mockk<Context>()

        val uriTest1Return = mockk<Uri>(relaxed = true)
        val uriTest2Return = mockk<Uri>(relaxed = true)
        coEvery { uriTest1Return.toString() }returns "content://com.example.provider/internal/1"
        coEvery { uriTest2Return.toString() }returns "content://com.example.provider/internal/2"
        coEvery { fileUtils.copyImageToInternalStorage(uriTest1,any())} returns uriTest1Return
        coEvery { fileUtils.copyImageToInternalStorage(uriTest2,any())} returns uriTest2Return

        viewModel.onImagesAdded(originalImagesUris, mockContext)

        val expectedPhotos = listOf(
            PropertyPhotosModel(photoPath = "content://com.example.provider/internal/1"),
            PropertyPhotosModel(photoPath = "content://com.example.provider/internal/2")
        )
        assertEquals(expectedPhotos, viewModel.state.photos)
    }

    @Test
    fun `onNearbyPlacesChanged should add or remove nearby place`() {
        val nearbyPlace1 = NearbyPlacesType.SCHOOL
        val nearbyPlace2 = NearbyPlacesType.PARC
        setAllRequiredFields()

        viewModel.onNearbyPlacesChanged(nearbyPlace1)
        viewModel.onNearbyPlacesChanged(nearbyPlace2)

        val expectedNearbyPlaces = listOf(nearbyPlace1, nearbyPlace2)
        assertEquals(expectedNearbyPlaces, viewModel.state.nearbyPlaces)

        viewModel.onNearbyPlacesChanged(nearbyPlace1)

        assertEquals(listOf(nearbyPlace2), viewModel.state.nearbyPlaces)
    }

    @Test
    fun `onIsAddressValidChanged should update isAddressValid`() {
        val startState = viewModel.isAddressValid

        viewModel.onIsAddressValidChanged()

        assertEquals(!startState, viewModel.isAddressValid)
    }

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
        val mockProperty = ProvideTestModelsOrRoomEntities.getTestPropertyModel()
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