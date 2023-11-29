package com.openclassrooms.realestatemanager.common.utils

object NumberUtils {
    fun convertToIntOrNull(value:String?):Int? {
        return if (value.isNullOrBlank()) null
        else {
            try {
                value.toInt()
            } catch (e: NumberFormatException) {
                null
            }
        }
    }
}