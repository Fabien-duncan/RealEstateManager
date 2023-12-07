package com.openclassrooms.realestatemanager.common.utils

import android.os.Build

class VersionProvider {
    open fun getSdkInt(): Int {
        return Build.VERSION.SDK_INT
    }
}