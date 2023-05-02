package ru.spiridonov.gallery.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.spiridonov.gallery.data.mapper.DtoMapper
import ru.spiridonov.gallery.data.network.ApiFactory
import ru.spiridonov.gallery.data.network.ApiService
import ru.spiridonov.gallery.data.storage.MediaStorage
import ru.spiridonov.gallery.domain.entity.Media
import ru.spiridonov.gallery.domain.repository.MediaRepository
import ru.spiridonov.gallery.utils.SharedPref
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
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
                        for (i in mediaList.indices) {
                            val media = mediaList[i]
                            downloadFile(media.file_location, false) { bitmap ->
                                media.photoFile = bitmap
                                mediaStorage.addMedia(media)
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
                        mediaStorage.addMedia(media)
                        Log.d("createPhotoMedia", "success")
                        Log.d("createPhotoMedia", media.toString())
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

    private fun createFileFromBitmap(bitmap: Bitmap): File? {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val date = dateFormat.format(calendar.time)
        val fileName = "IMG_$date.jpg"
        val file = File(application.cacheDir, fileName)
        file.createNewFile()

        val bos = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.JPEG, 100, bos)
        val bitmapData = bos.toByteArray()

        val fos: FileOutputStream?
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        }
        try {
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return file
    }
}