package ru.spiridonov.gallery.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import ru.spiridonov.gallery.data.mapper.DtoMapper
import ru.spiridonov.gallery.data.network.ApiFactory
import ru.spiridonov.gallery.data.network.ApiService
import ru.spiridonov.gallery.data.storage.MediaStorage
import ru.spiridonov.gallery.domain.entity.Media
import ru.spiridonov.gallery.domain.repository.MediaRepository
import ru.spiridonov.gallery.utils.SharedPref
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit
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

                val client = OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()
                val request: Request = Request.Builder()
                    .url("${ApiFactory.BASE_URL}media/download_media/$path/nobase?fileName=$mediaPath")
                    .method("GET", null)
                    .addHeader("Authorization", token)
                    .build()
                val response: Response = client.newCall(request).execute()

                response.body?.let { bodyRes ->
                    CoroutineScope(Dispatchers.Default).launch {
                        createFileFromBitmap(bodyRes.bytes(), mediaPath)?.let { file ->
                            var bitmap = BitmapFactory.decodeFile(file.absolutePath)
                            if(fullSize) bitmap = rotateImage(bitmap, file.path)
                            //if fullsize rotate image and save to cache/ do not download it. check it upper
                            // if not full size and no file in cache then delete file
                            // find in cache this file
                            // if not found, save to cache
                            // if found, return bitmap from cache
                            file.delete()
                            callback(bitmap)
                        }
                    }
                }

                /*

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

                 */
            } catch (e: Exception) {
                Log.d("downloadMedia", e.toString())
            }
        }
    }

    private fun createFileFromBitmap(byteArray: ByteArray, fileName: String): File? {
        val file = File(application.cacheDir, fileName)
        file.createNewFile()

        val fos: FileOutputStream?
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        }
        try {
            fos.write(byteArray)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return file
    }


    fun rotateImage(originalBitmap: Bitmap, path: String) =
        try {
            val exif = ExifInterface(path)
            val rotation: Int = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            val rotationInDegrees = exifToDegrees(rotation)
            val matrix = Matrix()
            if (rotation != 0) {
                matrix.preRotate(rotationInDegrees.toFloat())
            }
            Log.d("rotateImage", "rotation: $rotation")
            Log.d("showExif", showExif(exif))
            Bitmap.createBitmap(
                originalBitmap,
                0,
                0,
                originalBitmap.width,
                originalBitmap.height,
                matrix,
                true
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }


    private fun exifToDegrees(exifOrientation: Int) =
        when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }

    private fun showExif(exif: ExifInterface): String {
        var myAttribute: String? = "Exif information ---\n"
        myAttribute += getTagString(ExifInterface.TAG_DATETIME, exif)
        myAttribute += getTagString(ExifInterface.TAG_FLASH, exif)
        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE, exif)
        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE_REF, exif)
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE, exif)
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE_REF, exif)
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_LENGTH, exif)
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_WIDTH, exif)
        myAttribute += getTagString(ExifInterface.TAG_MAKE, exif)
        myAttribute += getTagString(ExifInterface.TAG_MODEL, exif)
        myAttribute += getTagString(ExifInterface.TAG_ORIENTATION, exif)
        myAttribute += getTagString(ExifInterface.TAG_WHITE_BALANCE, exif)
        return myAttribute.toString()
    }

    private fun getTagString(tag: String, exif: ExifInterface): String {
        return """$tag : ${exif.getAttribute(tag)}
"""
    }
}