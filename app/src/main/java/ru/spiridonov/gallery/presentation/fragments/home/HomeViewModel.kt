package ru.spiridonov.gallery.presentation.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.spiridonov.gallery.domain.entity.Media
import ru.spiridonov.gallery.domain.usecases.media_usecases.GetMediaFromAlbumUseCase
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getMediaFromAlbumUseCase: GetMediaFromAlbumUseCase
) : ViewModel() {
    private val _media = MutableLiveData<List<Media>>()
    val media: LiveData<List<Media>>
        get() = _media

    fun downloadAllMediaInfo() {
        if (_media.value.isNullOrEmpty())
            viewModelScope.launch {
                getMediaFromAlbumUseCase.invoke("all") {
                    _media.postValue(it)
                }
            }
    }
}