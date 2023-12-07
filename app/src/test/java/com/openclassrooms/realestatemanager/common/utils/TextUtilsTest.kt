package com.openclassrooms.realestatemanager.common.utils

import com.openclassrooms.realestatemanager.enums.CurrencyType
import junit.framework.TestCase.assertEquals
import org.junit.Test

class TextUtilsTest{
    @Test
    fun `capitaliseFirstLetter should capitalize the first letter of a non-empty string`() {
        val input = "test Text"

        val result = TextUtils.capitaliseFirstLetter(input)

        assertEquals("Test text", result)
    }
    @Test
    fun `capitaliseFirstLetter should return an empty string when given an empty string`() {
        val input = ""

        val result = TextUtils.capitaliseFirstLetter(input)

        assertEquals("", result)
    }
    @Test
    fun `priceFormat should format price with Dollar currency type`() {
        val price = 1000000
        val currencyType = CurrencyType.Dollar

        val result = TextUtils.priceFormat(price, currencyType)

        assertEquals("1,000,000", result)
    }

    @Test
    fun `priceFormat should format price with Euro currency type`() {
        val price = 1000000
        val currencyType = CurrencyType.Euro

        val result = TextUtils.priceFormat(price, currencyType)

        assertEquals("1 000 000", result)
    }
}