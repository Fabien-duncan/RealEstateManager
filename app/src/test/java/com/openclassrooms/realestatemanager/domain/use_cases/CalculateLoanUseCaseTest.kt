package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.domain.model.LoanModel
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CalculateLoanUseCaseTest{

    @Test
    fun calculateLoanShouldReturnTheCorrectMonthlyPayment(){
        val calculateLoanUseCase = CalculateLoanUseCase()

        val loan = LoanModel(10000.0,5.0,12)
        val result = calculateLoanUseCase.calculateLoan(loan)

        assertEquals(856.07,result,0.01)
    }
}