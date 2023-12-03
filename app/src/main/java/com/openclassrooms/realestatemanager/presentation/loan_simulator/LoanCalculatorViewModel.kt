package com.openclassrooms.realestatemanager.presentation.loan_simulator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.Utils
import com.openclassrooms.realestatemanager.common.utils.NumberUtils
import com.openclassrooms.realestatemanager.domain.model.LoanModel
import com.openclassrooms.realestatemanager.domain.use_cases.CalculateLoanUseCase

import javax.inject.Inject

class LoanCalculatorViewModel @Inject constructor(
    private val calculateLoanUseCase: CalculateLoanUseCase
) : ViewModel() {
    var state by mutableStateOf(LoanState())
        private set

    private val _monthlyPayment = MutableLiveData<Double>()
    val monthlyPayment: LiveData<Double> get() = _monthlyPayment
    fun calculateLoan(loanEntity: LoanModel) {
        val result = calculateLoanUseCase.calculateLoan(loanEntity)
        _monthlyPayment.value = result
    }
    fun onLoanAmountChanged(amount:String){
        val doubleAmount = NumberUtils.convertToDoubleOrNull(amount)

        state = state.copy(loanAmount = doubleAmount)
    }
    fun onInterestRateChanged(amount:String){
        val doubleAmount = NumberUtils.convertToDoubleOrNull(amount)

        state = state.copy(interestRate = doubleAmount)
    }
    fun onLoanTermChanged(amount:String){
        val intAmount = NumberUtils.convertToIntOrNull(amount)

        state = state.copy(loanTerm = intAmount)
    }

    fun checkDoubleAmountIsValid(amount:Double?):Boolean{
        return amount != null && amount >= 0
    }
    fun checkIntAmountIsValid(amount:Int?):Boolean{
        return amount != null && amount >= 0
    }
}
data class LoanState(
    val loanAmount: Double? = null,
    val interestRate: Double? = null,
    val loanTerm: Int? = null
)