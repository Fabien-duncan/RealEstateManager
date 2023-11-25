package com.openclassrooms.realestatemanager.common.utils

import com.openclassrooms.realestatemanager.enums.CurrencyType
import java.text.DecimalFormat
import java.util.Locale

object TextUtils {
    fun capitaliseFirstLetter(input: String): String {
        return if (input.isNotEmpty()) {
            val firstChar = input[0].uppercaseChar()
            val restOfChars = input.substring(1).lowercase(Locale.ROOT)
            "$firstChar$restOfChars"
        } else {
            return input
        }
    }
    fun priceFormat(price:Int, currencyType: CurrencyType):String{
        val formatter = DecimalFormat("#,###")

        val formattedPrice = when(currencyType){
            CurrencyType.Dollar -> formatter.format(price)
            CurrencyType.Euro -> formatter.format(price).replace(",", " ")
        }
        return formattedPrice
    }
}