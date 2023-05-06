package ru.spiridonov.gallery.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.spiridonov.gallery.data.mapper.DtoMapper
import ru.spiridonov.gallery.data.network.ApiService
import ru.spiridonov.gallery.data.storage.MediaStorage
import ru.spiridonov.gallery.domain.entity.Media
import ru.spiridonov.gallery.domain.repository.MediaRepository
import ru.spiridonov.gallery.utils.FileUtils
import ru.spiridonov.gallery.utils.ImageUtils
import ru.spiridonov.gallery.utils.SharedPref
import java.io.File
import javax.inject.Inject


class MediaRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPref,
    private val apiService: ApiService,
    private val mediaStorage: MediaStorage,
    private val dtoMapper: DtoMapper,
    private val application: Application
) : MediaRepository {
    override suspend fun getMediaFromAlbum(albumName: String, callback: (List<Media>) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val token = "Bearer " + sharedPref.getUser().accessToken
                apiService.getMediaFromAlbum(path = albumName, token = token).also { response ->
                    response.body()?.let { mediaJsonContainer ->
                        val mediaList =
                            dtoMapper.mapMediaListJsonContainerToMedia(mediaJsonContainer)
                                .toMutableList()
                        //sort medialist by dateCreated
                        mediaList.sortByDescending { it.date_created }
                        for (i in mediaList.indices) {
                            val media = mediaList[i]
                            downloadFile(media.file_location, false) { bitmap ->
                                media.photoFile = bitmap
                                mediaStorage.replaceMediaList(mediaList)
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

    override suspend fun createPhotoMedia(
        photo: File,
        location: String?,
        callback: (Boolean) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reqFile: RequestBody =
                    photo.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", photo.name, reqFile)
                val token = "Bearer " + sharedPref.getUser().accessToken

                apiService.uploadImage(token = token, file = body).also { response ->
                    response.body()?.let { mediaJson ->
                        val media = dtoMapper.mapMediaFileJsonContainerToMedia(mediaJson)
                        var bitmap = BitmapFactory.decodeFile(photo.absolutePath)
                        bitmap = ImageUtils.rotateImage(bitmap, photo.path) ?: bitmap
                        media.photoFile = bitmap
                        media.isInGoodQuality = true
                        mediaStorage.addMedia(media)
                        Log.d("createPhotoMedia", "success")
                        Log.d("createPhotoMedia", media.toString())
                        photo.delete()
                        callback(true)
                    }
                }
            } catch (e: Exception) {
                Log.d("createPhotoMedia", e.toString())
                callback(false)
            }
        }
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
                FileUtils.getFileFromCache(application, mediaPath)?.let { createdFile ->
                    var bitmap = BitmapFactory.decodeFile(createdFile.absolutePath)
                    bitmap = ImageUtils.rotateImage(bitmap, createdFile.path)
                    callback(bitmap)
                    return@launch
                }

                val token = "Bearer " + sharedPref.getUser().accessToken
                if (fullSize)
                    apiService.downloadMediaFile(
                        token = token,
                        path = "full",
                        fileName = mediaPath
                    )
                        .also { response ->
                            response.bytes().let { bodyRes ->
                                CoroutineScope(Dispatchers.Default).launch {
                                    FileUtils.createFileFromBitmap(
                                        application,
                                        bodyRes,
                                        mediaPath
                                    )?.let { file ->
                                        var bitmap = BitmapFactory.decodeFile(file.absolutePath)
                                        bitmap = ImageUtils.rotateImage(bitmap, file.path)
                                        callback(bitmap)
                                    }
                                }
                            }
                        }
                else
                    apiService.downloadMediaBase64(
                        token = token,
                        path = "thumbnail",
                        fileName = mediaPath
                    )
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