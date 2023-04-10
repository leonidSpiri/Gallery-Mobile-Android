package ru.spiridonov.gallery.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import ru.spiridonov.gallery.data.mapper.DtoMapper
import ru.spiridonov.gallery.data.network.ApiFactory
import ru.spiridonov.gallery.data.network.ApiService
import ru.spiridonov.gallery.domain.entity.Media
import ru.spiridonov.gallery.domain.repository.MediaRepository
import ru.spiridonov.gallery.utils.SharedPref
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MediaRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPref,
    private val apiService: ApiService,
    private val dtoMapper: DtoMapper
) : MediaRepository {
    override suspend fun getMediaFromAlbum(albumName: String, callback: (List<Media>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = "Bearer " + sharedPref.getUser().accessToken
                apiService.getMediaFromAlbum(path = albumName, token = token).also { response ->
                    response.body()?.let { mediaJsonContainer ->
                        callback(dtoMapper.mapMediaJsonContainerToMedia(mediaJsonContainer))
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


                val client = OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()
                val request: Request = Request.Builder()
                    .url("${ApiFactory.BASE_URL}media/download_media/$path?fileName=$mediaPath")
                    .method("GET", null)
                    .addHeader("Authorization", token)
                    .build()
                val response: Response = client.newCall(request).execute()

                response.body?.let { bodyRes ->
                    val bitmap = BitmapFactory.decodeStream(bodyRes.byteStream())
                    callback(bitmap)
                }

                /*
                *  apiService.downloadMedia(token = token, path = path, fileName = mediaPath)
                    .also { response ->
                        Log.d("UserRepositoryImpl", response)
                        response.let { stringFile ->
                            val bytes: ByteArray = stringFile.toByteArray()
                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            callback(bitmap)
                        }
                    }*/

            } catch (e: Exception) {
                Log.d("UserRepositoryImpl", e.toString())
            }
        }
    }
}