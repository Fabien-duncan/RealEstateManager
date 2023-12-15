package com.openclassrooms.realestatemanager.presentation.navigation

import com.openclassrooms.realestatemanager.enums.PropertyType
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class FilterViewModelTest{
    private lateinit var viewModel: FilterViewModel

    @Before
    fun setUp() {
        viewModel = FilterViewModel()
    }

    @Test
    fun `clearFilterState should reset state to default`() {
        viewModel.onAgentChanged("AgentName")
        viewModel.onTypeChange(PropertyType.APARTMENT)
        viewModel.onMinPriceChange("100")
        viewModel.onMaxPriceChange("200")

        viewModel.clearFilterState()

        val state = viewModel.state
        assertEquals(FilterState(), state)
    }
    @Test
    fun `onTypeChange should update propertyType in state`() {
        val propertyType = PropertyType.APARTMENT

        viewModel.onTypeChange(propertyType)

        val state = viewModel.state
        assertEquals(propertyType, state.propertyType)
    }
    @Test
    fun `onMinPriceChange should update minPrice in state`() {
        val number = "100"

        viewModel.onMinPriceChange(number)

        val state = viewModel.state
        assertEquals(100, state.minPrice)
    }

    @Test
    fun `onMaxPriceChange should update maxPrice in state`() {
        val number = "200"

        viewModel.onMaxPriceChange(number)

        val state = viewModel.state
        assertEquals(200, state.maxPrice)
    }
    @Test
    fun `onMinSurfaceChange should update minSurface in state`() {
        val number = "50"

        viewModel.onMinSurfaceChange(number)

        val state = viewModel.state
        assertEquals(50, state.minSurface)
    }

    @Test
    fun `onMaxSurfaceChange should update maxSurface in state`() {
        val number = "100"

        viewModel.onMaxSurfaceChange(number)

        val state = viewModel.state
        assertEquals(100, state.maxSurface)
    }

    @Test
    fun `onMinRoomsChange should update minRooms in state`() {
        val number = "2"

        viewModel.onMinRoomsChange(number)

        val state = viewModel.state
        assertEquals(2, state.minRooms)
    }

    @Test
    fun `onMaxRoomsChange should update maxRooms in state`() {
        val number = "5"

        viewModel.onMaxRoomsChange(number)

        val state = viewModel.state
        assertEquals(5, state.maxRooms)
    }
    @Test
    fun `onMinBathroomsChange should update minBathrooms in state`() {
        val number = "2"

        viewModel.onMinBathroomsChange(number)

        val state = viewModel.state
        assertEquals(2, state.minBathrooms)
    }

    @Test
    fun `onMaxBathroomsChange should update maxBathrooms in state`() {
        val number = "5"

        viewModel.onMaxBathroomsChange(number)

        val state = viewModel.state
        assertEquals(5, state.maxBathrooms)
    }
    @Test
    fun `onMinBedroomsChange should update minBedrooms in state`() {
        val number = "2"

        viewModel.onMinBathroomsChange(number)

        val state = viewModel.state
        assertEquals(2, state.minBathrooms)
    }

    @Test
    fun `onMaxBedroomsChange should update maxBedrooms in state`() {
        val number = "5"

        viewModel.onMaxBedroomsChange(number)

        val state = viewModel.state
        assertEquals(5, state.maxBedrooms)
    }


    @Test
    fun `onMinPhotosChange should update minPictures in state`() {
        val number = "3"

        viewModel.onMinPhotosChange(number)

        val state = viewModel.state
        assertEquals(3, state.minPictures)
    }


    @Test
    fun `onMinCreatedDateChanged should update minCreationDate in state and return true`() {
        val date = Date()

        val result = viewModel.onMinCreatedDateChanged(date)

        val state = viewModel.state
        assertEquals(date, state.minCreationDate)
        assertEquals(true, result)
    }

    @Test
    fun `onMaxCreatedDateChanged should update maxCreationDate in state and return true`() {
        val date = Date()

        val result = viewModel.onMaxCreatedDateChanged(date)

        val state = viewModel.state
        assertEquals(date, state.maxCreationDate)
        assertEquals(true, result)
    }
    @Test
    fun `onIsSoldChanged should update isSold in state`() {
        val isSold = true

        viewModel.onIsSoldChanged(isSold)

        val state = viewModel.state
        assertEquals(isSold, state.isSold)
    }

    @Test
    fun `onMinSoldDateChanged should update minSoldDate in state`() {
        val date = Date()

        viewModel.onMinSoldDateChanged(date)

        val state = viewModel.state
        assertEquals(date, state.minSoldDate)
    }

    @Test
    fun `onMaxSoldDateChanged should update maxSoldDate in state`() {
        val date = Date()

        viewModel.onMaxSoldDateChanged(date)

        val state = viewModel.state
        assertEquals(date, state.maxSoldDate)
    }
}