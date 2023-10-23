package com.openclassrooms.realestatemanager.common.utils

import java.util.Locale

object TextUtils {
    fun capitaliseFirstLetter(input: String): String {
        return if (input.isNotEmpty()) {
            val firstChar = input[0].uppercaseChar()
            val restOfChars = input.substring(1).lowercase(Locale.ROOT)
            "$firstChar$restOfChars"
        } else {
            return "No data was found!"
        }
    }
}