package com.openclassrooms.realestatemanager.common.utils
/**
 * Utility object for converting strings to numeric values in the application.
 */
object NumberUtils {
    /**
     * Converts a string representation of an integer to an Integer or returns null if the string is null, blank, or not a valid integer.
     *
     * @param value The string representation of an integer.
     * @return An Integer value or null if the conversion fails.
     */
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
    /**
     * Converts a string representation of a double to a Double or returns null if the string is null, blank, or not a valid double.
     *
     * @param value The string representation of a double.
     * @return A Double value or null if the conversion fails.
     */
    fun convertToDoubleOrNull(value:String?):Double? {
        return if (value.isNullOrBlank()) null
        else {
            try {
                value.toDouble()
            } catch (e: NumberFormatException) {
                null
            }
        }
    }
}