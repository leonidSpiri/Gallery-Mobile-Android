package ru.spiridonov.gallery.domain.usecases.media_usecases

import ru.spiridonov.gallery.domain.entity.Media
import ru.spiridonov.gallery.domain.repository.MediaRepository
import java.io.File
import javax.inject.Inject

class CreateMediaUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(media: Media, file: File, callback: (Boolean) -> Unit) =
        repository.createMedia(media, file, callback)
}