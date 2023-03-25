package ru.spiridonov.gallery.domain.repository

import ru.spiridonov.gallery.domain.entity.Media
import java.io.File

interface MediaRepository {
    suspend fun getMediaFromAlbum(albumId: String, callback: (List<Media>) -> Unit)

    suspend fun getMedia(id: String, callback: (Media?) -> Unit)

    suspend fun createMedia(media: Media, file: File, callback: (Boolean) -> Unit)

    suspend fun updateMedia(media: Media, callback: (Media?) -> Unit)

    suspend fun deleteMedia(id: String, callback: (Boolean) -> Unit)

    suspend fun downloadFile(id: String, fullSize: Boolean, callback: (File?) -> Unit)
}