package ru.spiridonov.gallery.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.spiridonov.gallery.domain.entity.User
import ru.spiridonov.gallery.domain.usecases.user_usecases.LoginUseCase
import ru.spiridonov.gallery.domain.usecases.user_usecases.RegisterUseCase
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private var loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    init {
        viewModelScope.launch {
            loginUseCase.invoke("email4@mail.com", "qwerty") { user, error ->
                _user.postValue(user)
                _error.postValue(error)
            }
            val userToRegister = User(
                user_id = "",
                email = "email4@mail.com",
                passwordHash = "qwerty",
                username = "a2",
                dateCreated = "",
                accessToken = ""
            )
            //registerUseCase.invoke(userToRegister) { user, error ->
            //    _user.postValue(user)
            //    _error.postValue(error)
            //}
        }
    }
}