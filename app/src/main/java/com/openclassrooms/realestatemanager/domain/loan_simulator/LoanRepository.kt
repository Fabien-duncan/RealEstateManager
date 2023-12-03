package com.openclassrooms.realestatemanager.domain.loan_simulator

import com.openclassrooms.realestatemanager.domain.model.LoanModel

interface LoanRepository {
    fun saveLoan(loanEntity: LoanModel)
    fun getLoan(): LoanModel
}