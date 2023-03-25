package ru.spiridonov.gallery.data.repository

import ru.spiridonov.gallery.domain.entity.User
import ru.spiridonov.gallery.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl@Inject constructor(): UserRepository{
    override suspend fun register(user: User, callback: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun login(email: String, password: String, callback: (User?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun logout(callback: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(callback: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getCurrentUser(): User? {
        TODO("Not yet implemented")
    }

}