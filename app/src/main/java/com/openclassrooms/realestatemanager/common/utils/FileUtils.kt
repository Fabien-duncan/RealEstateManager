package com.openclassrooms.realestatemanager.common.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

/**
 * A utility class for copying images to internal storage.
 *
 * @constructor Creates a FileUtils instance.
 */
class FileUtils {
    /**
     * Copies the provided image from external storage to internal storage.
     *
     * @param originalImageUri The URI of the original image in external storage.
     * @param context The application context.
     * @return The URI of the copied image in internal storage.
     */
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
        // Return the URI of the copied image
        return Uri.fromFile(File(context.filesDir, fileName))
    }
    //Retrieves the file name from the provided URI using the content resolver.
    private fun getFileNameFromUri(contentResolver: ContentResolver, uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)

        return cursor.use { outerCursor  ->
            outerCursor ?.let {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                it.moveToFirst()
                it.getString(nameIndex)
            }
        }
    }
}