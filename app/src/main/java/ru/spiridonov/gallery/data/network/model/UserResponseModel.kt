package ru.spiridonov.gallery.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserResponseModel(
    @SerializedName("error")
    @Expose
    val error: String? = null,

    @SerializedName("data")
    @Expose
    val data: ResponseUser,

    @SerializedName("message")
    @Expose
    val message: String? = null
)

data class ResponseUser(
    @SerializedName("user_id")
    @Expose
    val userId: String,

    @SerializedName("email")
    @Expose
    val email: String,

    @SerializedName("password_hash")
    @Expose
    val passwordHash: String,

    @SerializedName("username")
    @Expose
    val username: String,

    @SerializedName("date_created")
    @Expose
    val dateCreated: String,

    @SerializedName("access_token")
    @Expose
    val accessToken: String
)