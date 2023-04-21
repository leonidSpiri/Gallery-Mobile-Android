package ru.spiridonov.gallery.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DownloadPhotoResponseModel(
    @SerializedName("error")
    @Expose
    val error: String? = null,

    @SerializedName("data")
    @Expose
    val data: String,

    @SerializedName("message")
    @Expose
    val message: String? = null
)