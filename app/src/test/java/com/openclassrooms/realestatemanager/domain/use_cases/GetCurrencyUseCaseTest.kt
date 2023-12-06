package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.domain.currency_converter.CurrencyRepository
import com.openclassrooms.realestatemanager.enums.CurrencyType
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class GetCurrencyUseCaseTest{
    @MockK
    lateinit var mockRepository: CurrencyRepository

    lateinit var getCurrencyUseCase: GetCurrencyUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getCurrencyUseCase = GetCurrencyUseCase(mockRepository)
    }

    @Test
    fun `invoke should return selected currency`() {
        val mockCurrency = CurrencyType.Dollar
        every { mockRepository.getSelectedCurrency() } returns mockCurrency

        val result = getCurrencyUseCase()

        coVerify { mockRepository.getSelectedCurrency() }
        TestCase.assertEquals(mockCurrency, result)
    }

}