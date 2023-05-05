package ru.spiridonov.gallery.presentation.activity.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.spiridonov.gallery.domain.entity.User
import ru.spiridonov.gallery.domain.usecases.user_usecases.LoginUseCase
import ru.spiridonov.gallery.domain.usecases.user_usecases.RegisterUseCase
import javax.inject.Inject

class AccountViewModel @Inject constructor(
    private var loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _errorInputEmail = MutableLiveData<Boolean>()
    val errorInputEmail: LiveData<Boolean>
        get() = _errorInputEmail
    private val _errorInputPassword = MutableLiveData<Boolean>()
    val errorInputPassword: LiveData<Boolean>
        get() = _errorInputPassword
    private val _errorInputUsername = MutableLiveData<Boolean>()
    val errorInputUsername: LiveData<Boolean>
        get() = _errorInputUsername
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    fun login(email: String, password: String) =
        viewModelScope.launch {
            if (validateInput(email, password))
                loginUseCase.invoke(email, password) { user, error ->
                    if (error != null) {
                        _error.postValue(error)
                        if (error == "Password is not correct")
                            _errorInputPassword.postValue(true)
                        if (error == "User Not Exist")
                            _errorInputEmail.postValue(true)
                    } else
                        _user.postValue(user)
                }
        }

    fun register(user: User) = viewModelScope.launch {
        if (validateInput(user.email, user.passwordHash ?: "", user.username))
            registerUseCase.invoke(user) { user, error ->
                if (error != null) {
                    _error.postValue(error)
                    if (error == "User Already Exist")
                        _errorInputEmail.postValue(true)
                } else
                    _user.postValue(user)
            }
    }

    private fun validateInput(email: String, password: String, username: String = "emp"): Boolean {
        var result = true
        if (email.isBlank()) {
            _errorInputEmail.value = true
            result = false
        }
        if (password.isBlank() || password.length < 6) {
            _errorInputPassword.value = true
            result = false
        }
        if (username.isBlank() && username != "emp") {
            _errorInputUsername.value = true
            result = false
        }
        return result
    }

    fun parseStroke(input: String?) = input?.trim() ?: ""

    fun resetErrorInputEmail() {
        _errorInputEmail.value = false
    }

    fun resetErrorInputPassword() {
        _errorInputPassword.value = false
    }

    fun resetErrorInputUsername() {
        _errorInputUsername.value = false
    }
}