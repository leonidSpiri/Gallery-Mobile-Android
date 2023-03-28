package ru.spiridonov.gallery.domain.usecases.media_usecases

import ru.spiridonov.gallery.domain.repository.MediaRepository
import java.io.File
import javax.inject.Inject

class DownloadFileUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(id: String, fullSize: Boolean, callback: (File?) -> Unit) =
        repository.downloadFile(id, fullSize, callback)
}
