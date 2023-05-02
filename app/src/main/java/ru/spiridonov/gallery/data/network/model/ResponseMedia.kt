package ru.spiridonov.gallery.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseMedia(
    @SerializedName("media_id")
    @Expose
    val mediaId: String,

    @SerializedName("album_id")
    @Expose
    val albumId: String,

    @SerializedName("description")
    @Expose
    val description: String,

    @SerializedName("file_location")
    @Expose
    val fileLocation: String,

    @SerializedName("media_type")
    @Expose
    val mediaType: String,

    @SerializedName("date_created")
    @Expose
    val dateCreated: String,

    @SerializedName("geo_location")
    @Expose
    val geoLocation: String,

    @SerializedName("camera_info")
    @Expose
    val cameraInfo: String,

    @SerializedName("original_name")
    @Expose
    val originalName: String,

    @SerializedName("is_favourite")
    @Expose
    val isFavourite: Boolean,

    @SerializedName("is_deleted")
    @Expose
    val isDeleted: Boolean
)
