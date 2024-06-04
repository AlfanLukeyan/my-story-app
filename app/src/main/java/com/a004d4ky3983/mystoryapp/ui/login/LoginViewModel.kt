package com.a004d4ky3983.mystoryapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a004d4ky3983.mystoryapp.data.Result
import com.a004d4ky3983.mystoryapp.data.UserRepository
import com.a004d4ky3983.mystoryapp.data.preference.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isError = MutableLiveData<Boolean>()
    val isError: MutableLiveData<Boolean> = _isError

    private val _userToken = MutableLiveData<UserModel?>()
    val userToken: MutableLiveData<UserModel?> = _userToken

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            try {
                repository.saveSession(user)
                _isError.value = false
            } catch (e: Exception) {
                _isError.value = true
            }
        }
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val response = repository.login(email, password)
            _isLoading.value = false

            when (response) {
                is Result.Success -> {
                    val name: String = response.data.loginResult.name
                    val token: String = response.data.loginResult.token

                    _userToken.value = UserModel(name, token)
                    repository.updateToken(token)

                    saveSession(userToken.value!!)

                    _isError.value = false
                    _errorMessage.value = null
                }

                is Result.Error -> {
                    _isError.value = true
                    _errorMessage.value = response.error
                }

                is Result.Loading -> {
                    _isError.value = false
                    _errorMessage.value = null
                    _isLoading.value = true
                }
            }
        }
    }
}