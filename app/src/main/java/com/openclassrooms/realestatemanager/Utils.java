package com.openclassrooms.realestatemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {
    /**
     * Conversion of a Property price (Dollars to Euros)
     *
     * @param dollars The price in dollars
     * @return The converted price in euros
     */
    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * 0.812);
    }
    /**
     * Conversion of a Property price (Euros to Dollars)
     *
     * @param euros The price in euros
     * @return The converted price in dollars
     */
    public static int convertEuroToDollar(int euros) {
        return (int) Math.round(euros / 0.812);
    }
    /**
     * Conversion of today's date into a more suitable format
     *
     * @return The formatted string representing today's date
     */
    public static String getTodayDate() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }
    /**
     * Checking network connectivity
     * NOTE: DO NOT DELETE, TO BE SHOWN DURING THE DEFENSE
     *
     * @param context The application context
     * @return True if the internet is available, false otherwise
     */
    public static Boolean isInternetAvailable(Context context) {
        @SuppressLint("WifiManagerPotentialLeak") WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }
}
