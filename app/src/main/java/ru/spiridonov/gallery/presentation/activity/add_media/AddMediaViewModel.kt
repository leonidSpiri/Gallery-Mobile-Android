package ru.spiridonov.gallery.presentation.activity.add_media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.spiridonov.gallery.domain.usecases.media_usecases.CreatePhotoMediaUseCase
import ru.spiridonov.gallery.utils.FileUtils
import ru.spiridonov.gallery.utils.ImageUtils
import java.io.File
import javax.inject.Inject

class AddMediaViewModel @Inject constructor(
    private val createPhotoMediaUseCase: CreatePhotoMediaUseCase
) : ViewModel() {

    fun createBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        var bitmap = BitmapFactory.decodeStream(
            context.contentResolver.openInputStream(uri)
        )
        FileUtils.getPath(context, uri)?.let { path ->
            bitmap = ImageUtils.rotateImage(bitmap, path) ?: bitmap
            bitmap = ImageUtils.getResizedBitmap(bitmap) ?: bitmap
        }
        return bitmap
    }

    fun uploadPhoto(context: Context, imageUri: Uri, location: String) =
        try {
            FileUtils.getPath(context, imageUri)?.let { filePath ->
                val file = File(filePath)
                viewModelScope.launch {
                    createPhotoMediaUseCase.invoke(file, location) {

                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
}