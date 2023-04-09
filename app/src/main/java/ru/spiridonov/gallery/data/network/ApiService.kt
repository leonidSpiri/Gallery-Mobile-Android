package ru.spiridonov.gallery.data.network

import android.graphics.Bitmap
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import ru.spiridonov.gallery.data.network.model.MediaListResponseModel
import ru.spiridonov.gallery.data.network.model.UserResponseModel
import java.io.File

interface ApiService {

    @POST("users/login")
    suspend fun login(
        @Body requestBody: RequestBody
    ): Response<UserResponseModel>

    @POST("users/registration")
    suspend fun registration(
        @Body requestBody: RequestBody
    ): Response<UserResponseModel>

    @GET("media/media_list/{path}")
    suspend fun getMediaFromAlbum(
        @Header("Authorization") token: String,
        @Path(value = "path") path: String
    ): Response<MediaListResponseModel>

    @GET("media/download_media/{path}")
    suspend fun downloadMedia(
        @Header("Authorization") token: String,
        @Path(value = "path") path: String,
        @Query("fileName") fileName: String
    ): String
}