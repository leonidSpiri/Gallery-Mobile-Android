package ru.spiridonov.gallery.data.mapper

import com.google.gson.Gson
import okhttp3.internal.toLongOrDefault
import ru.spiridonov.gallery.data.network.model.MediaListResponseModel
import ru.spiridonov.gallery.data.network.model.UserResponseModel
import ru.spiridonov.gallery.domain.entity.Media
import ru.spiridonov.gallery.domain.entity.User
import javax.inject.Inject

class DtoMapper @Inject constructor() {
    fun mapUserJsonContainerToUser(jsonContainer: UserResponseModel) =
        User(
            user_id = jsonContainer.data.userId,
            email = jsonContainer.data.email,
            passwordHash = jsonContainer.data.passwordHash,
            username = jsonContainer.data.username,
            dateCreated = jsonContainer.data.dateCreated,
            accessToken = jsonContainer.data.accessToken
        )

    fun mapMediaJsonContainerToMedia(jsonContainer: MediaListResponseModel): List<Media> {
        val result = mutableListOf<Media>()
        jsonContainer.data.forEach { media ->
            result.add(
                Media(
                    media_id = media.mediaId,
                    album_id = media.albumId,
                    description = media.description,
                    file_location = media.fileLocation,
                    media_type = media.mediaType,
                    date_created = media.dateCreated.toLongOrDefault(0L),
                    geo_location = media.geoLocation,
                    camera_info = media.cameraInfo,
                    original_name = media.originalName,
                    is_favourite = media.isFavourite,
                    is_deleted = media.isDeleted
                )
            )
        }
        return result
    }
}