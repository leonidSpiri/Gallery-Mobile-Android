package ru.spiridonov.gallery.presentation.add_media

import android.graphics.Bitmap
import android.location.Location
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class AddMediaViewModel @Inject constructor(
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

    fun uploadPhoto(bitmap: Bitmap, location: String){

    }
}