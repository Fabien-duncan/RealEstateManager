package com.openclassrooms.realestatemanager.domain.currency_converter

import com.openclassrooms.realestatemanager.enums.CurrencyType

interface CurrencyRepository {
    fun getSelectedCurrency(): CurrencyType
    fun setSelectedCurrency(currencyType: CurrencyType)
    fun getPriceInCurrentCurrency(price:Int):String
}