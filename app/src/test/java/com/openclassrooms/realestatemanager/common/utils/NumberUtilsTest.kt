package com.openclassrooms.realestatemanager.common.utils

import junit.framework.TestCase.assertEquals
import org.junit.Test

class NumberUtilsTest{
    @Test
    fun `convertToIntOrNull should return null for blank or null input`() {
        val resultNullInput = NumberUtils.convertToIntOrNull(null)
        val resultBlankInput = NumberUtils.convertToIntOrNull("")

        assertEquals(null, resultNullInput)
        assertEquals(null, resultBlankInput)
    }

    @Test
    fun `convertToIntOrNull should return null for non-numeric input`() {
        val result = NumberUtils.convertToIntOrNull("abc")

        assertEquals(null, result)
    }

    @Test
    fun `convertToIntOrNull should return integer value for valid input`() {
        val result = NumberUtils.convertToIntOrNull("123")

        assertEquals(123, result)
    }

    @Test
    fun `convertToDoubleOrNull should return null for blank or null input`() {
        val resultNullInput = NumberUtils.convertToDoubleOrNull(null)
        val resultBlankInput = NumberUtils.convertToDoubleOrNull("")

        assertEquals(null, resultNullInput)
        assertEquals(null, resultBlankInput)
    }

    @Test
    fun `convertToDoubleOrNull should return null for non-numeric input`() {
        val result = NumberUtils.convertToDoubleOrNull("abc")

        assertEquals(null, result)
    }

    @Test
    fun `convertToDoubleOrNull should return null for an invalid input`() {
        val result = NumberUtils.convertToDoubleOrNull("123.45.6")

        assertEquals(null, result)
    }

    @Test
    fun `convertToDoubleOrNull should return double value for valid input`() {
        // When
        val result = NumberUtils.convertToDoubleOrNull("123.45")

        // Then
        assertEquals(123.45, result)
    }
}