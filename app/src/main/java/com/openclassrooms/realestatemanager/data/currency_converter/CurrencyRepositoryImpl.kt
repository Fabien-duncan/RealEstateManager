package com.openclassrooms.realestatemanager.data.currency_converter

import android.content.SharedPreferences
import com.openclassrooms.realestatemanager.domain.currency_converter.CurrencyRepository
import com.openclassrooms.realestatemanager.enums.CurrencyType
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
):CurrencyRepository {

    private val CURRENCY_KEY = "currency_key"
    override fun getSelectedCurrency(): CurrencyType {
        val currencyTypeName = sharedPreferences.getString(CURRENCY_KEY, null)
        return CurrencyType.valueOf(currencyTypeName ?: CurrencyType.Dollar.name)
    }

    override fun setSelectedCurrency(currencyType: CurrencyType) {
        sharedPreferences.edit().putString(CURRENCY_KEY, currencyType.name).apply()
    }
}