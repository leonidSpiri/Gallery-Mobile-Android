package ru.spiridonov.gallery.data.repository

import ru.spiridonov.gallery.domain.entity.Album
import ru.spiridonov.gallery.domain.repository.AlbumRepository
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor() : AlbumRepository {
    override suspend fun getAlbums(callback: (List<Album>) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun getAlbum(id: String, callback: (Album?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun createAlbum(album: Album, callback: (Album?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun updateAlbum(album: Album, callback: (Album?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlbum(id: String, callback: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }
}