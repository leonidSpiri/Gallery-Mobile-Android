package ru.spiridonov.gallery.data.mapper

import ru.spiridonov.gallery.data.network.model.UserResponseModel
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
}