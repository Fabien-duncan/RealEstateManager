package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.domain.model.LoanModel
import kotlin.math.pow

class CalculateLoanUseCase {
    fun calculateLoan(loanEntity: LoanModel): Double {
        val monthlyInterestRate = loanEntity.interestRate / 12 / 100
        val numberOfPayments = loanEntity.loanTerm
        val denominator = (1 + monthlyInterestRate).pow(numberOfPayments.toDouble()) - 1

        return loanEntity.loanAmount * (monthlyInterestRate + monthlyInterestRate / denominator)

    }
}