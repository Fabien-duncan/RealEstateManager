package com.openclassrooms.realestatemanager.common.utils

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.Date

class DateUtilsTest{
    @Test
    fun `formatDate returns expected date string`() {
        // Arrange
        val date = getDate(2022, Calendar.JANUARY, 15)
        val expectedFormattedDate = "15/01/2022"

        // Act
        val formattedDate = DateUtils.formatDate(date)

        // Assert
        assertEquals(expectedFormattedDate, formattedDate)
    }
    private fun getDate(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.time
    }

}