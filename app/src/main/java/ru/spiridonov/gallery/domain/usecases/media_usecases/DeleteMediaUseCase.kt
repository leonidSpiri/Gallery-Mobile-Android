package ru.spiridonov.gallery.domain.usecases.media_usecases

import ru.spiridonov.gallery.domain.repository.MediaRepository
import javax.inject.Inject

class DeleteMediaUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(id: String, callback: (Boolean) -> Unit) =
        repository.deleteMedia(id, callback)
}
