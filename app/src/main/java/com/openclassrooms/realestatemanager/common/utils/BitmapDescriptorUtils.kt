package com.openclassrooms.realestatemanager.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object BitmapDescriptorUtils {

    fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorResourceId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResourceId) ?: return null

        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun bitmapDescriptorFromJpg(context: Context, @DrawableRes jpgResourceId: Int): BitmapDescriptor? {
        val jpgBitmap = BitmapFactory.decodeResource(context.resources, jpgResourceId)
        return BitmapDescriptorFactory.fromBitmap(jpgBitmap)
    }
}