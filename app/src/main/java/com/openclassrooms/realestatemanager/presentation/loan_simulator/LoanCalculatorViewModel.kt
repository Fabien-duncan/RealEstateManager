package com.openclassrooms.realestatemanager.presentation.loan_simulator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.common.utils.NumberUtils
import com.openclassrooms.realestatemanager.domain.model.LoanModel
import com.openclassrooms.realestatemanager.domain.use_cases.CalculateLoanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel class for the Loan Calculator screen, managing loan calculation and state.
 *
 * @property calculateLoanUseCase Use case for calculating loan details.
 */
@HiltViewModel
class LoanCalculatorViewModel @Inject constructor(
    private val calculateLoanUseCase: CalculateLoanUseCase
) : ViewModel() {
    // MutableStateFlow representing the state of the Loan Calculator screen.
    var state by mutableStateOf(LoanState())
        private set
    // Mutable state representing the validity of the loan calculation form.
    var isFormValid by mutableStateOf(false)
        private set
    // Mutable state representing the calculated monthly payment used for the output
    var monthlyPayment by mutableStateOf<Double?>(null)
        private set

    /**
     * Gets the [LoanModel] instance based on the current state.
     */
    private val loanModel: LoanModel
        get() = state.run {
            LoanModel(
                loanAmount = loanAmount!!,
                interestRate = interestRate!!,
                loanTerm = loanTerm!!
            )
        }
    /**
     * Clears the loan state, resetting the form and monthly payment.
     */
    fun clearLoanState(){
        state = LoanState()
        isFormValid = false
        monthlyPayment = null
    }

    /**
     * Sets the loan amount in the state.
     *
     * @param amount The loan amount.
     */
    fun setLoanAmount(amount: Double){
        state = state.copy(loanAmount = amount)
    }
    /**
     * Handles changes in the loan amount input.
     *
     * @param amount The loan amount as a string.
     */
    fun onLoanAmountChanged(amount:String){
        val doubleAmount = NumberUtils.convertToDoubleOrNull(amount)

        if (amount.isNotEmpty() && doubleAmount != null) {
            state = state.copy(loanAmount = doubleAmount)
        }
        setIsFormValid()
    }
    /**
     * Handles changes in the interest rate input.
     *
     * @param amount The interest rate as a string.
     */
    fun onInterestRateChanged(amount:String){
        val doubleAmount = NumberUtils.convertToDoubleOrNull(amount)

        if (amount.isNotEmpty() && doubleAmount != null) {
            state = state.copy(interestRate = doubleAmount)
        }

        setIsFormValid()
    }
    /**
     * Handles changes in the loan term input.
     *
     * @param amount The loan term as a string.
     */
    fun onLoanTermChanged(amount:String){
        val intAmount = NumberUtils.convertToIntOrNull(amount)

        state = state.copy(loanTerm = intAmount)

        setIsFormValid()
    }

    /**
     * Checks if the double amount is valid and updates the form validity state.
     *
     * @param amount The double amount to check.
     * @return `true` if the amount is valid, `false` otherwise.
     */
    fun checkDoubleAmountIsValid(amount:Double?):Boolean{
        return if (amount != null && amount > 0) true
        else{
            isFormValid = false
            false
        }
    }
    /**
     * Checks if the int amount is valid and updates the form validity state.
     *
     * @param amount The int amount to check.
     * @return `true` if the amount is valid, `false` otherwise.
     */
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
/**
 * Data class representing the state of the Loan Calculator screen.
 *
 * @property loanAmount The loan amount.
 * @property interestRate The interest rate.
 * @property loanTerm The loan term.
 */
data class LoanState(
    val loanAmount: Double? = null,
    val interestRate: Double? = null,
    val loanTerm: Int? = null
)