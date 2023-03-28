package ru.spiridonov.gallery.domain.entity

data class User(
    val user_id: String,
    val email: String,
    val password_hash: String? = null,
    val username: String,
    val date_created: String,
    val access_token: String
)
