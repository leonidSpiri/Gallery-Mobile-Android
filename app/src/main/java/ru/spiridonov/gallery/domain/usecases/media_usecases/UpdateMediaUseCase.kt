package ru.spiridonov.gallery.domain.usecases.media_usecases

import ru.spiridonov.gallery.domain.entity.Media
import ru.spiridonov.gallery.domain.repository.MediaRepository
import javax.inject.Inject

class UpdateMediaUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(media: Media, callback: (Media?) -> Unit) =
        repository.updateMedia(media, callback)
}
