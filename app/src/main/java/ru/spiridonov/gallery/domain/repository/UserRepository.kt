package ru.spiridonov.gallery.domain.repository

import ru.spiridonov.gallery.domain.entity.User

interface UserRepository {
    suspend fun register(user: User, callback: (Boolean) -> Unit)

    suspend fun login(email: String, password: String, callback: (User?) -> Unit)

    suspend fun logout(callback: (Boolean) -> Unit)

    suspend fun deleteAccount(callback: (Boolean) -> Unit)

    fun getCurrentUser(): User?
}