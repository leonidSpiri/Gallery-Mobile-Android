package ru.spiridonov.gallery.domain.usecases.user_usecases

import ru.spiridonov.gallery.domain.entity.User
import ru.spiridonov.gallery.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User, callback: (User?, String?) -> Unit) =
        repository.register(user, callback)
}
