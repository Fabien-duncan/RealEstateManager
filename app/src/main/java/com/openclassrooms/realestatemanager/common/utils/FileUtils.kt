package com.openclassrooms.realestatemanager.common.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date

object FileUtils {
    fun copyImageToInternalStorage(originalImageUri: Uri, context: Context): Uri {
        val originalFileName = getFileNameFromUri(context.contentResolver, originalImageUri)
        val fileName = "copied_image_$originalFileName"

        val inputStream = context.contentResolver.openInputStream(originalImageUri)
        val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)

        inputStream?.use { input ->
            outputStream?.use { output ->
                input.copyTo(output)
            }
        }
        // Return the URI to the copied image
        return Uri.fromFile(File(context.filesDir, fileName))
    }
    private fun getFileNameFromUri(contentResolver: ContentResolver, uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)

        return cursor.use { cursor ->
            cursor?.let {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                it.moveToFirst()
                it.getString(nameIndex)
            }
        }
    }
}