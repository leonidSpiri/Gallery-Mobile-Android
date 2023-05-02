package ru.spiridonov.gallery.presentation.add_media

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.loader.content.CursorLoader
import kotlinx.coroutines.launch
import ru.spiridonov.gallery.domain.usecases.media_usecases.CreatePhotoMediaUseCase
import java.io.File
import javax.inject.Inject

class AddMediaViewModel @Inject constructor(
    private val application: Application,
    private val createPhotoMediaUseCase: CreatePhotoMediaUseCase
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


    fun uploadPhoto(photo: File, location: String) =
        viewModelScope.launch {
            createPhotoMediaUseCase.invoke(photo, location) {

            }
        }

    fun getPath(uri: Uri): String? {
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

    private fun showExif(exif: ExifInterface): String {
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