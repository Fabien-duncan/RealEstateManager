package com.openclassrooms.realestatemanager.data.currency_converter

import android.content.SharedPreferences
import com.openclassrooms.realestatemanager.Utils
import com.openclassrooms.realestatemanager.common.utils.TextUtils
import com.openclassrooms.realestatemanager.domain.currency_converter.CurrencyRepository
import com.openclassrooms.realestatemanager.enums.CurrencyType
import javax.inject.Inject
/**
 * Implementation of [CurrencyRepository] that manages currency-related operations and preferences.
 *
 * @param sharedPreferences The storage for accessing and modifying currency-related preferences.
 */
class CurrencyRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
):CurrencyRepository {
    private val CURRENCY_KEY = "currency_key"
    /**
     * Retrieves the user-selected currency type from shared preferences.
     *
     * @return The [CurrencyType] representing the user-selected currency.
     */
    override fun getSelectedCurrency(): CurrencyType {
        val currencyTypeName = sharedPreferences.getString(CURRENCY_KEY, null)
        return CurrencyType.valueOf(currencyTypeName ?: CurrencyType.Dollar.name)
    }

    /**
     * Sets the user-selected currency type in shared preferences.
     *
     * @param currencyType The [CurrencyType] to be set as the user-selected currency.
     */
    override fun setSelectedCurrency(currencyType: CurrencyType) {
        sharedPreferences.edit().putString(CURRENCY_KEY, currencyType.name).apply()
    }

    /**
     * Formats the price in the current selected currency.
     *
     * @param price The price to be formatted.
     * @return A formatted string representing the price in the selected currency.
     */
    override fun getPriceInCurrentCurrency(price: Int): String {
        return when(getSelectedCurrency()) {
            CurrencyType.Euro -> "${TextUtils.priceFormat(Utils.convertDollarToEuro(price), CurrencyType.Euro)}€"
            CurrencyType.Dollar -> "\$${TextUtils.priceFormat(price, CurrencyType.Dollar)}"
        }
    }
}