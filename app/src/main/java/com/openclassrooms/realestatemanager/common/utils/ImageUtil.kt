package com.openclassrooms.realestatemanager.common.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
/**
 * Utility object for working with images and icons in the application.
 */
object ImageUtil {
    /**
     * Converts a vector drawable to a BitmapDescriptor for use in Google Maps markers.
     *
     * @param context The application context.
     * @param vectorResId The resource ID of the vector drawable.
     * @return A BitmapDescriptor representing the vector drawable as a bitmap.
     */
    fun bitmapDescriptorFromVector(
        context: Context,
        vectorResId: Int
    ): BitmapDescriptor? {

        // retrieve the actual drawable
        val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val bm = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // draw it onto the bitmap
        val canvas = android.graphics.Canvas(bm)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bm)
    }
}