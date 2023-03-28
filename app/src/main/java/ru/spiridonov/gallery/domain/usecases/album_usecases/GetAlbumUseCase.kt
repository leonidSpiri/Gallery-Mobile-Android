package ru.spiridonov.gallery.domain.usecases.album_usecases

import ru.spiridonov.gallery.domain.entity.Album
import ru.spiridonov.gallery.domain.repository.AlbumRepository
import javax.inject.Inject

class GetAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    suspend operator fun invoke(id: String, callback: (Album?) -> Unit) =
        repository.getAlbum(id, callback)
}
