package com.openclassrooms.realestatemanager.data.currency_converter

import android.content.SharedPreferences
import com.openclassrooms.realestatemanager.Utils
import com.openclassrooms.realestatemanager.common.utils.TextUtils
import com.openclassrooms.realestatemanager.enums.CurrencyType
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class CurrencyRepositoryImplTest{
    @MockK
    lateinit var mockSharedPreferences: SharedPreferences

    private lateinit var currencyRepositoryImpl: CurrencyRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        currencyRepositoryImpl = CurrencyRepositoryImpl(mockSharedPreferences)
    }

    @Test
    fun `getSelectedCurrency should return default currency if not set`() {
        every { mockSharedPreferences.getString(any(), any()) } returns null

        val result = currencyRepositoryImpl.getSelectedCurrency()

        assertEquals(CurrencyType.Dollar, result)
    }

    @Test
    fun `getSelectedCurrency should return stored currency`() {
        val storedCurrency = CurrencyType.Euro
        every { mockSharedPreferences.getString(any(), any()) } returns storedCurrency.name

        val result = currencyRepositoryImpl.getSelectedCurrency()

        assertEquals(storedCurrency, result)
    }

    @Test
    fun `setSelectedCurrency should store selected currency`() {
        val selectedCurrency = CurrencyType.Euro
        val sharedPreferencesEditor = mockk<SharedPreferences.Editor>(relaxed = true)

        every { mockSharedPreferences.edit() } returns sharedPreferencesEditor

        currencyRepositoryImpl.setSelectedCurrency(selectedCurrency)

        verify {mockSharedPreferences.edit()}
    }
    @Test
    fun `getPriceInCurrentCurrency should return price in Euro`() {
        val priceInDollar = 100
        val expectedPriceInEuro = Utils.convertDollarToEuro(priceInDollar)
        every { mockSharedPreferences.getString(any(), any()) } returns CurrencyType.Euro.name

        val result = currencyRepositoryImpl.getPriceInCurrentCurrency(priceInDollar)

        assertEquals("${TextUtils.priceFormat(expectedPriceInEuro, CurrencyType.Euro)}€", result)
    }
    @Test
    fun `getPriceInCurrentCurrency should return price in Dollar`() {

        val priceInDollar = 100
        every { mockSharedPreferences.getString(any(), any()) } returns CurrencyType.Dollar.name

        val result = currencyRepositoryImpl.getPriceInCurrentCurrency(priceInDollar)

        assertEquals("\$${TextUtils.priceFormat(priceInDollar, CurrencyType.Dollar)}", result)
    }

}