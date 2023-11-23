package com.openclassrooms.realestatemanager.common.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PermissionHelper(private val context: Context) {

    // Function to check if a particular permission is granted
    fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    // Function to request a single permission
    suspend fun requestPermission(permission: String): Boolean {
        return withContext(Dispatchers.Main) {
            // Check if the permission is already granted
            if (isPermissionGranted(permission)) {
                return@withContext true
            }

            // Use suspendCoroutine to request permission asynchronously
            suspendCoroutine { continuation ->
                val callback = object : PermissionCallback {
                    override fun onResult(isGranted: Boolean) {
                        continuation.resume(isGranted)
                    }
                }

                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(permission),
                    PERMISSION_REQUEST_CODE,
                )

                permissionCallbacks[permission] = callback
            }
        }
    }

    // Callback for handling permission results
    private interface PermissionCallback {
        fun onResult(isGranted: Boolean)
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 42

        // Map to store permission callbacks based on the requested permission
        private val permissionCallbacks = mutableMapOf<String, PermissionCallback?>()

        // Function to handle permission results
        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            if (requestCode == PERMISSION_REQUEST_CODE) {
                for (i in permissions.indices) {
                    val permission = permissions[i]
                    val callback = permissionCallbacks.remove(permission)

                    // Notify the callback about the result
                    callback?.onResult(grantResults[i] == PackageManager.PERMISSION_GRANTED)
                }
            }
        }
    }
}