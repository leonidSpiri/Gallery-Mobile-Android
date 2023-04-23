package ru.spiridonov.gallery.presentation.ui.fullscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.spiridonov.gallery.data.storage.MediaStorage
import ru.spiridonov.gallery.domain.entity.Media
import javax.inject.Inject

class FullScreenViewModel @Inject constructor(
    private val mediaStorage: MediaStorage
) : ViewModel() {
    private val _media = MutableLiveData<Media>()
    val media: LiveData<Media>
        get() = _media

    fun setMediaId(mediaId: String) {
        _media.value = mediaStorage.getMediaById(mediaId)
    }
}