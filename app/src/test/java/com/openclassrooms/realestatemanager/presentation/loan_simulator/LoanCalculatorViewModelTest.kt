package com.openclassrooms.realestatemanager.presentation.loan_simulator

import com.openclassrooms.realestatemanager.MainCoroutineRule
import com.openclassrooms.realestatemanager.ProvideTestProperties
import com.openclassrooms.realestatemanager.domain.model.LoanModel
import com.openclassrooms.realestatemanager.domain.model.PropertyModel
import com.openclassrooms.realestatemanager.domain.use_cases.CalculateLoanUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.CheckInternetConnectionUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetPropertyByIdUseCase
import com.openclassrooms.realestatemanager.presentation.detail.DetailViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoanCalculatorViewModelTest{
    @MockK
    private lateinit var calculateLoanUseCase: CalculateLoanUseCase

    private lateinit var viewModel: LoanCalculatorViewModel
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = LoanCalculatorViewModel(calculateLoanUseCase)
        viewModel.onLoanAmountChanged("10000.0")
        viewModel.onInterestRateChanged("5.0")
        viewModel.onLoanTermChanged("12")
    }

    @Test
    fun `calculateLoan should update monthlyPayment`() = runBlocking{
        val expectedMonthlyPayment = 850.0

        coEvery { calculateLoanUseCase.calculateLoan(any()) } returns expectedMonthlyPayment

        viewModel.calculateLoan()

        assertEquals(expectedMonthlyPayment, viewModel.monthlyPayment)
    }

    @Test
    fun `checkDoubleAmountIsValid should return true for valid amount`() {
        val result = viewModel.checkDoubleAmountIsValid(10000.0)

        assertTrue(result)
        assertTrue(viewModel.isFormValid)
    }

    @Test
    fun `checkDoubleAmountIsValid should return false for invalid amount`() {
        val result = viewModel.checkDoubleAmountIsValid(0.0)

        assertFalse(result)
        assertFalse(viewModel.isFormValid)
    }

    @Test
    fun `checkIntAmountIsValid should return true for valid amount`() {
        val result = viewModel.checkIntAmountIsValid(12)

        assertTrue(result)
        assertTrue(viewModel.isFormValid)
    }

    @Test
    fun `checkIntAmountIsValid should return false for invalid amount`() {
        val result = viewModel.checkIntAmountIsValid(0)

        assertFalse(result)
        assertFalse(viewModel.isFormValid)
    }
}