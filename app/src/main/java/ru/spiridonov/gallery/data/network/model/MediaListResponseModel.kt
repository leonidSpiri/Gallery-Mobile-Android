package ru.spiridonov.gallery.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MediaListResponseModel(
    @SerializedName("error")
    @Expose
    val error: String? = null,

    @SerializedName("data")
    @Expose
    val data: List<ResponseMedia>,

    @SerializedName("message")
    @Expose
    val message: String? = null
)