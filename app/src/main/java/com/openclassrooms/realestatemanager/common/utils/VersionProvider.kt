package com.openclassrooms.realestatemanager.common.utils

import android.os.Build

/**
 * Class used to provide the sdk version to a repository to help with testability
 */
open class VersionProvider {
    open fun getSdkInt(): Int {
        return Build.VERSION.SDK_INT
    }
}