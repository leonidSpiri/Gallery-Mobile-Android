package ru.spiridonov.gallery.domain.repository

import ru.spiridonov.gallery.domain.entity.Album

interface AlbumRepository {
    suspend fun getAlbums(callback: (List<Album>) -> Unit)

    suspend fun getAlbum(id: String, callback: (Album?) -> Unit)

    suspend fun createAlbum(album: Album, callback: (Album?) -> Unit)

    suspend fun updateAlbum(album: Album, callback: (Album?) -> Unit)

    suspend fun deleteAlbum(id: String, callback: (Boolean) -> Unit)
}