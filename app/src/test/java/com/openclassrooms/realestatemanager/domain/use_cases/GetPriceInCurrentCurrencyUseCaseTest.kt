package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.domain.Connection.ConnectionCheckerRepository
import com.openclassrooms.realestatemanager.domain.currency_converter.CurrencyRepository
import com.openclassrooms.realestatemanager.enums.CurrencyType
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class GetPriceInCurrentCurrencyUseCaseTest{
    @MockK
    lateinit var mockRepository: CurrencyRepository

    lateinit var getPriceInCurrentCurrencyUseCase: GetPriceInCurrentCurrencyUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getPriceInCurrentCurrencyUseCase = GetPriceInCurrentCurrencyUseCase(mockRepository)
    }

    @Test
    fun `invoke should return price in current currency`() {
        val mockPriceInCurrentCurrency = "$1,000,500"
        every {
            mockRepository.getPriceInCurrentCurrency(
                price = any()
            )
        } returns mockPriceInCurrentCurrency

        val result = getPriceInCurrentCurrencyUseCase(
            price = 1000000
        )

        coVerify { mockRepository.getPriceInCurrentCurrency(any()) }
        assertEquals(mockPriceInCurrentCurrency, result)
    }
}