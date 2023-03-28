package ru.spiridonov.gallery.domain.usecases.user_usecases

import ru.spiridonov.gallery.domain.entity.User
import ru.spiridonov.gallery.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String, callback: (User?, String?) -> Unit) =
        repository.login(email, password, callback)
}