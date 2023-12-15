package com.openclassrooms.realestatemanager.common.utils

import java.text.SimpleDateFormat
import java.util.Date

object DateUtils {
    fun formatDate(date: Date): String{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.format(date)
    }
}