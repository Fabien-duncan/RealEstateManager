package com.openclassrooms.realestatemanager.common.utils

import com.openclassrooms.realestatemanager.enums.CurrencyType
import java.text.DecimalFormat
import java.util.Locale
/**
 * Utility object for text-related formatting for the presentation layer
 */
object TextUtils {
    /**
     * Capitalizes the first letter of the input string and puts the lower case the rest.
     *
     * @param input The input string.
     * @return Returns a new string with the first letter capitalized.
     */
    fun capitaliseFirstLetter(input: String): String {
        return if (input.isNotEmpty()) {
            val firstChar = input[0].uppercaseChar()
            val restOfChars = input.substring(1).lowercase(Locale.ROOT)
            "$firstChar$restOfChars"
        } else {
            return input
        }
    }
    /**
     * Formats the given price according to the specified currency type.
     *
     * @param price The price to be formatted.
     * @param currencyType The currency type (e.g., Dollar or Euro).
     * @return Returns the formatted price as a string.
     */
    fun priceFormat(price:Int, currencyType: CurrencyType):String{
        val formatter = DecimalFormat("#,###")

        val formattedPrice = when(currencyType){
            CurrencyType.Dollar -> formatter.format(price)
            CurrencyType.Euro -> formatter.format(price).replace(",", " ")
        }
        return formattedPrice
    }
}