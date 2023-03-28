package ru.spiridonov.gallery.domain.usecases.media_usecases

import ru.spiridonov.gallery.domain.entity.Media
import ru.spiridonov.gallery.domain.repository.MediaRepository
import javax.inject.Inject

class GetMediaUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(id: String, callback: (Media?) -> Unit) =
        repository.getMedia(id, callback)
}
