package ru.spiridonov.gallery.presentation.add_media

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.loader.content.CursorLoader
import java.lang.reflect.Executable
import javax.inject.Inject

class AddMediaViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {

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

    fun rotateImage(originalBitmap: Bitmap, imageUri: Uri) =
        try {
            getPath(imageUri)?.let { path ->
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
                Bitmap.createBitmap(
                    originalBitmap,
                    0,
                    0,
                    originalBitmap.width,
                    originalBitmap.height,
                    matrix,
                    true
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }


    fun uploadPhoto(bitmap: Bitmap, location: String) {

    }

    private fun getPath(uri: Uri): String? {
        val data = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(application, uri, data, null, null, null)
        val cursor = loader.loadInBackground()
        cursor?.let {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        }
        return null
    }

    private fun exifToDegrees(exifOrientation: Int) =
        when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
}