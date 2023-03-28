package ru.spiridonov.gallery.domain.repository

import ru.spiridonov.gallery.domain.entity.User

interface UserRepository {
    suspend fun register(user: User, callback: (User?, String?) -> Unit)

    suspend fun login(email: String, password: String, callback: (User?, String?) -> Unit)

    suspend fun logout(callback: (Boolean, String?) -> Unit)

    suspend fun deleteAccount(callback: (Boolean, String?) -> Unit)

    fun getCurrentUser(): User?
}