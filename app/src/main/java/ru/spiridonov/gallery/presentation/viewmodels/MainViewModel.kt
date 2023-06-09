package ru.spiridonov.gallery.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.spiridonov.gallery.domain.entity.User
import ru.spiridonov.gallery.domain.usecases.user_usecases.GetCurrentUserUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user

    fun getCurrentUser() = viewModelScope.launch {
        _user.postValue(getCurrentUserUseCase.invoke())
    }
}