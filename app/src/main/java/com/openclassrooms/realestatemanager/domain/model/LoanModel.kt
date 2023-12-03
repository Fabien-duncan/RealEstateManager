package com.openclassrooms.realestatemanager.domain.model

data class LoanModel(
    val loanAmount: Double,
    val interestRate: Double,
    val loanTerm: Int
)