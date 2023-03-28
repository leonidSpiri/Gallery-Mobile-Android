package ru.spiridonov.gallery.domain.usecases.user_usecases

import ru.spiridonov.gallery.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    fun invoke() = repository.getCurrentUser()
}