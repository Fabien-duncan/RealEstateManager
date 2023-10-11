package com.openclassrooms.realestatemanager;

import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class UtilsTest extends TestCase {

    public void testConvertDollarToEuro() {
        assertEquals(812, Utils.convertDollarToEuro(1000));
        assertEquals(10, Utils.convertDollarToEuro(12));
    }

    public void testConvertEuroToDollar() {
        assertEquals(1000, Utils.convertEuroToDollar(812));
        assertEquals(12, Utils.convertEuroToDollar(10));
    }

    public void testGetTodayDate() {
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        assertEquals(today.format(formatter), Utils.getTodayDate());
    }

    public void testIsInternetAvailable() {

    }
}