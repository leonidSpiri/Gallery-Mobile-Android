package ru.spiridonov.gallery.data.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import ru.spiridonov.gallery.data.network.model.DownloadPhotoResponseModel
import ru.spiridonov.gallery.data.network.model.MediaFileResponseModel
import ru.spiridonov.gallery.data.network.model.MediaListResponseModel
import ru.spiridonov.gallery.data.network.model.UserResponseModel

interface ApiService {

    @POST("users/login")
    suspend fun login(
        @Body requestBody: RequestBody
    ): Response<UserResponseModel>

    @POST("users/registration")
    suspend fun registration(
        @Body requestBody: RequestBody
    ): Response<UserResponseModel>

    @Multipart
    @POST("media/upload_media")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Response<MediaFileResponseModel>

    @GET("media/media_list/{path}")
    suspend fun getMediaFromAlbum(
        @Header("Authorization") token: String,
        @Path(value = "path") path: String
    ): Response<MediaListResponseModel>

    @GET("media/download_media/{path}/base")
    suspend fun downloadMedia(
        @Header("Authorization") token: String,
        @Path(value = "path") path: String,
        @Query("fileName") fileName: String
    ): Response<DownloadPhotoResponseModel>
}