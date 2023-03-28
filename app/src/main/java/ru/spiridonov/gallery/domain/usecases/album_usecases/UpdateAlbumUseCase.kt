package ru.spiridonov.gallery.domain.usecases.album_usecases

import ru.spiridonov.gallery.domain.entity.Album
import ru.spiridonov.gallery.domain.repository.AlbumRepository
import javax.inject.Inject

class UpdateAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    suspend operator fun invoke(album: Album, callback: (Album?) -> Unit) =
        repository.updateAlbum(album, callback)
}
