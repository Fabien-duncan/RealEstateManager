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
    fun downloadStaticMapImage(imageUrl: String): Bitmap {
        val url = URL(imageUrl)
        val connection = url.openConnection()
        val inputStream: InputStream = connection.getInputStream()
        return BitmapFactory.decodeStream(inputStream)
    }
    fun saveImageToInternalStorage(context: Context, bitmap: Bitmap, fileName: String) {
        val folder = File(context.filesDir, "MapsImages")
        folder.mkdirs()

        val file = File(folder, fileName)
        val outputStream = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        outputStream.flush()
        outputStream.close()

        println("Maps images is at: ${Uri.fromFile(file)}")
    }
    private fun getFileNameFromUri(contentResolver: ContentResolver, uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)

        return try {
            cursor?.let {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                it.moveToFirst()
                it.getString(nameIndex)
            }
        } finally {
            cursor?.close()
        }
    }
}