package com.openclassrooms.realestatemanager.domain.Connection

interface ConnectionCheckerRepository {
    fun isInternetConnected():Boolean
}