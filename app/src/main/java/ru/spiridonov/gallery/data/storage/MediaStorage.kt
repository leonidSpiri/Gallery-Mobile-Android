package ru.spiridonov.gallery.data.storage

import ru.spiridonov.gallery.domain.entity.Media

object MediaStorage {
    private var mediaList = mutableListOf<Media>()

    fun addMedia(media: Media) {
        mediaList.add(media)
        mediaList.sortByDescending { it.date_created }
    }

    fun updateMedia(media: Media) {
        val index = mediaList.indexOfFirst { it.media_id == media.media_id }
        mediaList[index] = media
    }

    fun replaceMediaList(list: List<Media>) {
        mediaList = list.toMutableList()
    }

    fun getMediaList() =
        mediaList

    fun getMediaById(id: String) =
        mediaList.find { it.media_id == id }

    fun getMediaCount() =
        mediaList.size

    fun clearMediaList() =
        mediaList.clear()

}