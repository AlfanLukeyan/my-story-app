package com.a004d4ky3983.mystoryapp.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a004d4ky3983.mystoryapp.data.Result
import com.a004d4ky3983.mystoryapp.data.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isError = MutableLiveData<Boolean>()
    val isError: MutableLiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun addStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.uploadStory(file, description, lat, lon)
            _isLoading.value = false

            when (result) {
                is Result.Success -> {
                    _isError.value = false
                    _errorMessage.value = null
                }

                is Result.Error -> {
                    _isError.value = true
                    _errorMessage.value = result.error
                }

                is Result.Loading -> {
                    _isError.value = false
                    _errorMessage.value = null
                }
            }
        }
    }
}