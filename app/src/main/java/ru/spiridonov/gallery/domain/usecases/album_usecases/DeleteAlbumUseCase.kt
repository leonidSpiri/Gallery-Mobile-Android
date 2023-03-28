package ru.spiridonov.gallery.domain.usecases.album_usecases

import ru.spiridonov.gallery.domain.repository.AlbumRepository
import javax.inject.Inject

class DeleteAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    suspend operator fun invoke(id: String, callback: (Boolean) -> Unit) =
        repository.deleteAlbum(id, callback)
}
