package ru.spiridonov.gallery.domain.entity

import android.graphics.Bitmap
import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Media(
    val media_id: String,
    val album_id: String,
    val description: String,
    val file_location: String,
    val media_type: String,
    val date_created: Long,
    val geo_location: String,
    val camera_info: String,
    var original_name: String,
    val is_favourite: Boolean,
    val is_deleted: Boolean,
    var photoFile: Bitmap? = null,
    var isInGoodQuality: Boolean = false
) : Parcelable
