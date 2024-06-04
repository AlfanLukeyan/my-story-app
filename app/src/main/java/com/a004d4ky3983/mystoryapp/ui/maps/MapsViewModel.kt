package com.a004d4ky3983.mystoryapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a004d4ky3983.mystoryapp.data.Result
import com.a004d4ky3983.mystoryapp.data.UserRepository
import com.a004d4ky3983.mystoryapp.data.remote.response.ListStoryItem
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStories

    private var isDataLoaded = false

    init {
        getStoriesLocation()
    }

    private fun getStoriesLocation() {
        _isLoading.value = true
        viewModelScope.launch {
            val response = repository.getStoriesLocation()
            _isLoading.value = false

            when (response) {
                is Result.Success -> {
                    _listStories.value = response.data.listStory
                    _isError.value = false
                    _errorMessage.value = null
                    isDataLoaded = true
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