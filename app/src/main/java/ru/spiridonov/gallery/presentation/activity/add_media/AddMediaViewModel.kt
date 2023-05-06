package ru.spiridonov.gallery.presentation.activity.add_media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _wasMediaUpload = MutableLiveData<Boolean>()
    val wasMediaUpload: LiveData<Boolean>
        get() = _wasMediaUpload


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
                Log.d("myTag", "filePath: $filePath")
                val file = File(filePath)
                viewModelScope.launch {
                    createPhotoMediaUseCase.invoke(file, location) {
                        _wasMediaUpload.postValue(it)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
}