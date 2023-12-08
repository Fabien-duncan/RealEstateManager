package com.openclassrooms.realestatemanager.presentation.navigation

import com.openclassrooms.realestatemanager.Utils
import com.openclassrooms.realestatemanager.domain.Connection.ConnectionCheckerRepository
import com.openclassrooms.realestatemanager.domain.use_cases.CheckInternetConnectionUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetCurrencyUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetPriceInCurrentCurrencyUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.SetCurrencyUseCase
import com.openclassrooms.realestatemanager.enums.CurrencyType
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class CurrencyViewModelTest{
    @MockK
    lateinit var setCurrencyUseCase: SetCurrencyUseCase
    @MockK
    lateinit var getCurrencyUseCase: GetCurrencyUseCase
    @MockK
    lateinit var getPriceInCurrentCurrencyUseCase: GetPriceInCurrentCurrencyUseCase

    lateinit var viewModel: CurrencyViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { getCurrencyUseCase.invoke() } returns CurrencyType.Dollar
        viewModel = CurrencyViewModel(setCurrencyUseCase,getCurrencyUseCase,getPriceInCurrentCurrencyUseCase)
    }

    @Test
    fun `setCurrency should invoke setCurrencyUseCase and update currentCurrency`() {
        val newCurrency = CurrencyType.Euro
        every { setCurrencyUseCase.invoke(newCurrency) } returns Unit
        every { getCurrencyUseCase.invoke() } returns newCurrency

        viewModel.setCurrency(newCurrency)

        assertEquals(newCurrency, viewModel.currentCurrency)
    }

    @Test
    fun `getCurrency should update currentCurrency`() {
        val currentCurrency = CurrencyType.Dollar
        every { getCurrencyUseCase.invoke() } returns currentCurrency

        viewModel.getCurrency()

        assertEquals(currentCurrency, viewModel.currentCurrency)
    }

    @Test
    fun `getPriceInCurrentCurrency with an Int should invoke getPriceInCurrentCurrencyUseCase`() {
        val price = 100
        val expectedResult = "100"
        every { getPriceInCurrentCurrencyUseCase.invoke(price) } returns expectedResult

        val result = viewModel.getPriceInCurrentCurrency(price)

        coVerify { getPriceInCurrentCurrencyUseCase.invoke(any()) }
        assertEquals(expectedResult, result)
    }

    @Test
    fun `getPriceInCurrentCurrency with an Int for Euro should convert price to Euro`() {
        viewModel.currentCurrency = CurrencyType.Euro
        val price = 100
        val expectedEuroPrice = "81.2"
        every { getPriceInCurrentCurrencyUseCase.invoke(price) } returns expectedEuroPrice

        val result = viewModel.getPriceInCurrentCurrency(price)

        assertEquals(expectedEuroPrice, result)
    }

    @Test
    fun `getPriceInCurrentCurrency with an Int for Dollar should return the same price`() {
        val price = 100
        val expectedResult = "100"
        every { getPriceInCurrentCurrencyUseCase.invoke(price) } returns expectedResult

        val result = viewModel.getPriceInCurrentCurrency(price)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `getPriceInCurrentCurrency with a Double for Euro should convert price to Euro`() {
        viewModel.currentCurrency = CurrencyType.Euro
        val price = 100.0

        val result = viewModel.getPriceInCurrentCurrency(price)

        assertEquals(81.2, result, 0.5)
    }
    @Test
    fun `getPriceInCurrentCurrency with a Double for Dollar should return the same price`() {
        viewModel.currentCurrency = CurrencyType.Dollar
        val price = 100.0

        val result = viewModel.getPriceInCurrentCurrency(price)

        assertEquals(price, result, 0.5)
    }
}