package com.openclassrooms.realestatemanager.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.Utils
import com.openclassrooms.realestatemanager.domain.use_cases.GetCurrencyUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetPriceInCurrentCurrencyUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.SetCurrencyUseCase
import com.openclassrooms.realestatemanager.enums.CurrencyType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel class for managing currency-related operations.
 *
 * @property setCurrencyUseCase Use case for setting the current currency type.
 * @property getCurrencyUseCase Use case for getting the current currency type.
 * @property getPriceInCurrentCurrencyUseCase Use case for converting prices to the current currency.
 */
@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val setCurrencyUseCase: SetCurrencyUseCase,
    private val getCurrencyUseCase: GetCurrencyUseCase,
    private val getPriceInCurrentCurrencyUseCase: GetPriceInCurrentCurrencyUseCase
):ViewModel(){
    /**
     * The current currency type used in the application.
     */
    var currentCurrency by mutableStateOf(CurrencyType.Dollar)

    /**
     * Initializes the ViewModel by retrieving the current currency.
     */
    init {
        getCurrency()
    }
    /**
     * Sets the current currency type and updates the UI.
     *
     * @param currencyType The new currency type to set.
     */
    fun setCurrency(currencyType: CurrencyType){
        setCurrencyUseCase.invoke(currencyType)
        getCurrency()
    }
    /**
     * Retrieves the current currency type.
     */
    fun getCurrency() {
        currentCurrency = getCurrencyUseCase.invoke()
    }
    /**
     * Converts the given price in Int to the current currency type and formats it accordingly.
     *
     * @param price The price in the original currency.
     * @return The converted price in the current currency in a formatted way.
     */
    fun getPriceInCurrentCurrency(price:Int) = getPriceInCurrentCurrencyUseCase.invoke(price)
    /**
     * Overloaded method, Converts the given price in double to the current currency type and formats it accordingly.
     *
     * @param price The price in the original currency.
     * @return The converted price in the current currency.
     */
    fun getPriceInCurrentCurrency(price:Double):Double{
        val priceInt = price.toInt()
        return when(currentCurrency) {
            CurrencyType.Euro -> Utils.convertDollarToEuro(priceInt).toDouble()
            CurrencyType.Dollar -> price
        }

    }
}