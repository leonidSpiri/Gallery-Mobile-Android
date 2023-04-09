package ru.spiridonov.gallery.domain.usecases.media_usecases

import android.graphics.Bitmap
import ru.spiridonov.gallery.domain.repository.MediaRepository
import java.io.File
import javax.inject.Inject

class DownloadFileUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(mediaPath: String, fullSize: Boolean, callback: (Bitmap?) -> Unit) =
        repository.downloadFile(mediaPath, fullSize, callback)
}
