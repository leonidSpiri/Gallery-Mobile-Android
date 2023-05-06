package ru.spiridonov.gallery.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.exifinterface.media.ExifInterface

object ImageUtils {

    fun getResizedBitmap(image: Bitmap, maxSize: Int = 1024): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    fun rotateImage(originalBitmap: Bitmap, path: String) =
        try {
            val exif = ExifInterface(path)
            val rotation: Int = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            val rotationInDegrees = exifToDegrees(rotation)
            val matrix = Matrix()
            if (rotation != 0) {
                matrix.preRotate(rotationInDegrees.toFloat())
            }
            Log.d("rotateImage", "rotation: $rotation")
            Log.d("showExif", showExif(exif))
            Bitmap.createBitmap(
                originalBitmap,
                0,
                0,
                originalBitmap.width,
                originalBitmap.height,
                matrix,
                true
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }


    fun exifToDegrees(exifOrientation: Int) =
        when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }

    fun showExif(exif: ExifInterface): String {
        var myAttribute: String? = "Exif information ---\n"
        myAttribute += getTagString(ExifInterface.TAG_DATETIME, exif)
        myAttribute += getTagString(ExifInterface.TAG_FLASH, exif)
        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE, exif)
        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE_REF, exif)
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE, exif)
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE_REF, exif)
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_LENGTH, exif)
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_WIDTH, exif)
        myAttribute += getTagString(ExifInterface.TAG_MAKE, exif)
        myAttribute += getTagString(ExifInterface.TAG_MODEL, exif)
        myAttribute += getTagString(ExifInterface.TAG_ORIENTATION, exif)
        myAttribute += getTagString(ExifInterface.TAG_WHITE_BALANCE, exif)
        return myAttribute.toString()
    }

    private fun getTagString(tag: String, exif: ExifInterface): String {
        return """$tag : ${exif.getAttribute(tag)}
"""
    }
}