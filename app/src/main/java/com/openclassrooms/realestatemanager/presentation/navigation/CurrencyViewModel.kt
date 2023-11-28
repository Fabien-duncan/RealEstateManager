package com.openclassrooms.realestatemanager.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.Utils
import com.openclassrooms.realestatemanager.common.utils.TextUtils
import com.openclassrooms.realestatemanager.domain.use_cases.GetCurrencyUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.GetPriceInCurrentCurrencyUseCase
import com.openclassrooms.realestatemanager.domain.use_cases.SetCurrencyUseCase
import com.openclassrooms.realestatemanager.enums.CurrencyType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val setCurrencyUseCase: SetCurrencyUseCase,
    private val getCurrencyUseCase: GetCurrencyUseCase,
    private val getPriceInCurrentCurrencyUseCase: GetPriceInCurrentCurrencyUseCase
):ViewModel(){
    var currentCurrency by mutableStateOf(CurrencyType.Dollar)

    init {
        getCurrency()
    }
    fun setCurrency(currencyType: CurrencyType){
        setCurrencyUseCase.invoke(currencyType)
        getCurrency()
    }
    fun getCurrency() {
        currentCurrency = getCurrencyUseCase.invoke()
    }
    fun getPriceInCurrentCurrency(price:Int) = getPriceInCurrentCurrencyUseCase.invoke(price)
}