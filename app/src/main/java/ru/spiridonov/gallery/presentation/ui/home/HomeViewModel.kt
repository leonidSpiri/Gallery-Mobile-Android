package ru.spiridonov.gallery.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.spiridonov.gallery.domain.entity.User
import ru.spiridonov.gallery.domain.usecases.user_usecases.GetCurrentUserUseCase
import ru.spiridonov.gallery.domain.usecases.user_usecases.LoginUseCase
import ru.spiridonov.gallery.domain.usecases.user_usecases.RegisterUseCase
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user

    fun downloadData() =
        viewModelScope.launch {
            _user.postValue(getCurrentUserUseCase.invoke())
        }
}