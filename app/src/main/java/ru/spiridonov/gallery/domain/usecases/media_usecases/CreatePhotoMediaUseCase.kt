package ru.spiridonov.gallery.domain.usecases.media_usecases

import ru.spiridonov.gallery.domain.repository.MediaRepository
import java.io.File
import javax.inject.Inject

class CreatePhotoMediaUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(photo: File, location: String?, callback: (Boolean) -> Unit) =
        repository.createPhotoMedia(photo, location, callback)
}