package ru.spiridonov.gallery.utils

import android.app.Application
import android.content.Context
import ru.spiridonov.gallery.domain.entity.User
import javax.inject.Inject

class SharedPref @Inject constructor(
    private val application: Application,
) {

    private fun getUnnamedSharedPref(name: String, key: String): String? =
        application.getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, "")

    private fun setUnnamedSharedPref(name: String, key: String, value: String) =
        application.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString(key, value)
            .apply()


    fun saveUser(user: User) {
        setUnnamedSharedPref("user_info", "uuid", user.user_id)
        setUnnamedSharedPref("user_info", "email", user.email)
        user.password_hash?.let { setUnnamedSharedPref("user_info", "password_hash", it) }
        setUnnamedSharedPref("user_info", "username", user.username)
        setUnnamedSharedPref("user_info", "date_created", user.date_created)
        setUnnamedSharedPref("user_info", "access_token", user.access_token)
    }

    fun getUser() =
        User(
            user_id = getUnnamedSharedPref("user_info", "uuid") ?: "",
            email = getUnnamedSharedPref("user_info", "email") ?: "",
            password_hash = getUnnamedSharedPref("user_info", "password_hash"),
            username = getUnnamedSharedPref("user_info", "username") ?: "",
            date_created = getUnnamedSharedPref("user_info", "date_created") ?: "",
            access_token = getUnnamedSharedPref("user_info", "access_token") ?: "",
        )

    fun clearUser() =
        application.getSharedPreferences("user_info", Context.MODE_PRIVATE).edit().clear().apply()
}