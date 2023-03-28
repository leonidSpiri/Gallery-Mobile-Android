package ru.spiridonov.gallery.data.repository

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import ru.spiridonov.gallery.data.mapper.DtoMapper
import ru.spiridonov.gallery.data.network.ApiService
import ru.spiridonov.gallery.data.network.model.UserResponseModel
import ru.spiridonov.gallery.domain.entity.User
import ru.spiridonov.gallery.domain.repository.UserRepository
import ru.spiridonov.gallery.utils.SharedPref
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPref,
    private val apiService: ApiService,
    private val dtoMapper: DtoMapper
) : UserRepository {
    override suspend fun register(user: User, callback: (User?, String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val jsonObject = JSONObject()
                jsonObject.put("userName", user.username)
                jsonObject.put("userEmail", user.email)
                jsonObject.put("userPassword", user.passwordHash)
                val jsonObjectString = jsonObject.toString()
                val requestBody =
                    jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
                apiService.registration(requestBody).also {
                    parseUserResponseModel(it, callback)
                }
            } catch (e: Exception) {
                Log.d("UserRepositoryImpl", e.toString())
                callback(null, e.toString())
            }
        }
    }

    override suspend fun login(
        email: String,
        password: String,
        callback: (User?, String?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val jsonObject = JSONObject()
                jsonObject.put("userEmail", email)
                jsonObject.put("userPassword", password)
                val jsonObjectString = jsonObject.toString()
                val requestBody =
                    jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
                apiService.login(requestBody).also {
                    parseUserResponseModel(it, callback)
                }
            } catch (e: Exception) {
                Log.d("UserRepositoryImpl", e.toString())
                callback(null, e.toString())
            }
        }
    }

    override suspend fun logout(callback: (Boolean, String?) -> Unit) =
        sharedPref.clearUser().also { callback(true, null) }

    override suspend fun deleteAccount(callback: (Boolean, String?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getCurrentUser(): User? {
        val user = sharedPref.getUser()
        return if (user.user_id.isNotEmpty()) user else null
    }

    private fun parseUserResponseModel(
        response: Response<UserResponseModel>,
        callback: (User?, String?) -> Unit
    ) {
        if (response.body() == null || !response.isSuccessful) {
            Log.d("UserRepositoryImpl", "response.code" + response.code().toString())
            response.errorBody()?.charStream()?.readText().also {
                it?.let { errorJson ->
                    val error = JSONObject(errorJson).getString("message")
                    callback(null, error)
                }
            }
            return
        }
        val user = dtoMapper.mapUserJsonContainerToUser(response.body()!!)
        sharedPref.saveUser(user)
        callback(user, null)
    }

}