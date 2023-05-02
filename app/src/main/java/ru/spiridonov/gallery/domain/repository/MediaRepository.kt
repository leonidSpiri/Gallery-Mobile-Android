package ru.spiridonov.gallery.domain.repository

import android.graphics.Bitmap
import ru.spiridonov.gallery.domain.entity.Media
import java.io.File

interface MediaRepository {
    suspend fun getMediaFromAlbum(albumName: String, callback: (List<Media>) -> Unit)

    suspend fun getMedia(id: String, callback: (Media?) -> Unit)

    suspend fun createPhotoMedia(photo: File, location: String?, callback: (Boolean) -> Unit)

    suspend fun updateMedia(media: Media, callback: (Media?) -> Unit)

    suspend fun deleteMedia(id: String, callback: (Boolean) -> Unit)

    suspend fun downloadFile(mediaPath: String, fullSize: Boolean, callback: (Bitmap?) -> Unit)
}