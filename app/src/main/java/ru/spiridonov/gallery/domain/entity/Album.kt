package ru.spiridonov.gallery.domain.entity

data class Album(
    val album_id: String,
    val user_id: String,
    val description: String,
    val avatar_location: String
)