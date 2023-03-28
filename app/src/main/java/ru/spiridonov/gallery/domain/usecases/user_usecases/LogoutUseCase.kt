package ru.spiridonov.gallery.domain.usecases.user_usecases

import ru.spiridonov.gallery.domain.repository.UserRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(callback: (Boolean, String?) -> Unit) =
        repository.logout(callback)
}