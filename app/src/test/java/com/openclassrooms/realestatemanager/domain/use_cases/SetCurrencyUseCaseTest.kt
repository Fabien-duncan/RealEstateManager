package com.openclassrooms.realestatemanager.domain.use_cases
import com.openclassrooms.realestatemanager.domain.currency_converter.CurrencyRepository
import com.openclassrooms.realestatemanager.enums.CurrencyType
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

class SetCurrencyUseCaseTest{
    @MockK
    lateinit var mockRepository: CurrencyRepository

    private lateinit var setCurrencyUseCase: SetCurrencyUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        setCurrencyUseCase = SetCurrencyUseCase(mockRepository)
    }

    @Test
    fun `invoke should set selected currency`() {
        coEvery {
            mockRepository.setSelectedCurrency(
                currencyType = CurrencyType.Euro
            )
        } returns Unit

        val setCurrencyUseCase = SetCurrencyUseCase(mockRepository)

        setCurrencyUseCase(CurrencyType.Euro)

        coVerify { mockRepository.setSelectedCurrency(currencyType = CurrencyType.Euro)}
    }

}