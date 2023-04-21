package ru.spiridonov.gallery.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.spiridonov.gallery.data.mapper.DtoMapper
import ru.spiridonov.gallery.data.network.ApiService
import ru.spiridonov.gallery.domain.entity.Media
import ru.spiridonov.gallery.domain.repository.MediaRepository
import ru.spiridonov.gallery.utils.SharedPref
import java.io.File
import javax.inject.Inject


class MediaRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPref,
    private val apiService: ApiService,
    private val dtoMapper: DtoMapper
) : MediaRepository {
    override suspend fun getMediaFromAlbum(albumName: String, callback: (List<Media>) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val token = "Bearer " + sharedPref.getUser().accessToken
                apiService.getMediaFromAlbum(path = albumName, token = token).also { response ->
                    response.body()?.let { mediaJsonContainer ->
                        val mediaList = dtoMapper.mapMediaJsonContainerToMedia(mediaJsonContainer)
                            .toMutableList()
                        for (i in mediaList.indices) {
                            val media = mediaList[i]
                            downloadFile(media.file_location, false) { bitmap ->
                                media.photoFile = bitmap
                                callback(mediaList)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("UserRepositoryImpl", e.toString())
            }
        }
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

    override suspend fun downloadFile(
        mediaPath: String,
        fullSize: Boolean,
        callback: (Bitmap?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = "Bearer " + sharedPref.getUser().accessToken
                val path = if (fullSize) "full" else "thumbnail"

                apiService.downloadMedia(token = token, path = path, fileName = mediaPath)
                    .also { response ->
                        response.body()?.let { bodyRes ->
                            CoroutineScope(Dispatchers.Default).launch {
                                val decodedString: ByteArray =
                                    Base64.decode(bodyRes.data, Base64.DEFAULT)
                                val decodedByte = BitmapFactory.decodeByteArray(
                                    decodedString,
                                    0,
                                    decodedString.size
                                )
                                callback(decodedByte)
                            }
                        }
                    }

            } catch (e: Exception) {
                Log.d("downloadMedia", e.toString())
            }
        }
    }
}