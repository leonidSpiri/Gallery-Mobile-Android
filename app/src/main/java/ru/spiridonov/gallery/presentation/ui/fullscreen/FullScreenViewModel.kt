package ru.spiridonov.gallery.presentation.ui.fullscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.spiridonov.gallery.data.storage.MediaStorage
import ru.spiridonov.gallery.domain.entity.Media
import ru.spiridonov.gallery.domain.usecases.media_usecases.DownloadFileUseCase
import javax.inject.Inject

class FullScreenViewModel @Inject constructor(
    private val mediaStorage: MediaStorage,
    private val downloadFileUseCase: DownloadFileUseCase
) : ViewModel() {
    private val _media = MutableLiveData<Media>()
    val media: LiveData<Media>
        get() = _media

    fun setMediaId(mediaId: String) = viewModelScope.launch {
        mediaStorage.getMediaById(mediaId)?.let { thumbMedia ->
            _media.postValue(thumbMedia)
            if (thumbMedia.isInGoodQuality) return@launch
            downloadFileUseCase.invoke(thumbMedia.file_location, true) { fullBitmap ->
                val fullMedia = thumbMedia.copy(photoFile = fullBitmap, isInGoodQuality = true)
                _media.postValue(fullMedia)
                mediaStorage.updateMedia(fullMedia)
            }
        }
    }
}