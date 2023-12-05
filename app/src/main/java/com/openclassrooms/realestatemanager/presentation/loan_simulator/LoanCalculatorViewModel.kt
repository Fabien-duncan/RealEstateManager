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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow

import javax.inject.Inject
@HiltViewModel
class LoanCalculatorViewModel @Inject constructor(
    private val calculateLoanUseCase: CalculateLoanUseCase
) : ViewModel() {
    var state by mutableStateOf(LoanState())
        private set

    var isFormValid by mutableStateOf(false)
        private set

    var monthlyPayment by mutableStateOf<Double?>(null)
        private set

    private val loanModel: LoanModel
        get() = state.run {
            LoanModel(
                loanAmount = loanAmount!!,
                interestRate = interestRate!!,
                loanTerm = loanTerm!!
            )
        }

    fun clearLoanState(){
        state = LoanState()
        isFormValid = false
        monthlyPayment = null
    }

    fun setLoanAmount(amount: Double){
        state = state.copy(loanAmount = amount)
    }
    fun onLoanAmountChanged(amount:String){
        val doubleAmount = NumberUtils.convertToDoubleOrNull(amount)

        if (amount.isNotEmpty() && doubleAmount != null) {
            state = state.copy(loanAmount = doubleAmount)
        }
        setIsFormValid()
    }
    fun onInterestRateChanged(amount:String){
        val doubleAmount = NumberUtils.convertToDoubleOrNull(amount)

        if (amount.isNotEmpty() && doubleAmount != null) {
            state = state.copy(interestRate = doubleAmount)
        }

        setIsFormValid()
    }
    fun onLoanTermChanged(amount:String){
        val intAmount = NumberUtils.convertToIntOrNull(amount)

        state = state.copy(loanTerm = intAmount)

        setIsFormValid()
    }

    fun checkDoubleAmountIsValid(amount:Double?):Boolean{
        return if (amount != null && amount > 0) true
        else{
            isFormValid = false
            false
        }
    }
    fun checkIntAmountIsValid(amount:Int?):Boolean{
        return if (amount != null && amount > 0) true
        else{
            isFormValid = false
            false
        }
    }
    private fun setIsFormValid(){
        isFormValid = validateLoanState()
    }
    private fun validateLoanState() = state.loanAmount != null && state.loanTerm != null && state.interestRate != null
    fun calculateLoan(){
        monthlyPayment= calculateLoanUseCase.calculateLoan(loanModel)
    }
}
data class LoanState(
    val loanAmount: Double? = null,
    val interestRate: Double? = null,
    val loanTerm: Int? = null
)