package ru.spiridonov.gallery.domain.usecases.media_usecases

import ru.spiridonov.gallery.domain.entity.Media
import ru.spiridonov.gallery.domain.repository.MediaRepository
import javax.inject.Inject

class GetMediaFromAlbumUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(albumId: String, callback: (List<Media>) -> Unit) =
        repository.getMediaFromAlbum(albumId, callback)
}
