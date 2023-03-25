package ru.spiridonov.gallery.data.repository

import ru.spiridonov.gallery.domain.entity.Media
import ru.spiridonov.gallery.domain.repository.MediaRepository
import java.io.File
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor() : MediaRepository {
    override suspend fun getMediaFromAlbum(albumId: String, callback: (List<Media>) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun getMedia(id: String, callback: (Media?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun createMedia(media: Media, file: File, callback: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun updateMedia(media: Media, callback: (Media?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMedia(id: String, callback: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun downloadFile(id: String, fullSize: Boolean, callback: (File?) -> Unit) {
        TODO("Not yet implemented")
    }
}