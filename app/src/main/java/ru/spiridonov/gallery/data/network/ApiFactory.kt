package ru.spiridonov.gallery.data.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiFactory {
    const val BASE_URL = "http://192.168.1.144:3000/"
    //const val BASE_URL = "http://192.158.1.71:3000/"

    private val json = GsonBuilder()
        .setLenient()
        .create()

    private val okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(json))
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
