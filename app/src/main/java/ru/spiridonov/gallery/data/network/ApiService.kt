package ru.spiridonov.gallery.data.network

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
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
}