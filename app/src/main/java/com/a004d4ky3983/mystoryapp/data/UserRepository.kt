package com.a004d4ky3983.mystoryapp.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.a004d4ky3983.mystoryapp.R
import com.a004d4ky3983.mystoryapp.data.data.StoryRemoteMediator
import com.a004d4ky3983.mystoryapp.data.local.StoryDatabase
import com.a004d4ky3983.mystoryapp.data.preference.UserModel
import com.a004d4ky3983.mystoryapp.data.preference.UserPreference
import com.a004d4ky3983.mystoryapp.data.remote.response.FileUploadResponse
import com.a004d4ky3983.mystoryapp.data.remote.response.ListStoryItem
import com.a004d4ky3983.mystoryapp.data.remote.response.LoginResponse
import com.a004d4ky3983.mystoryapp.data.remote.response.RegisterResponse
import com.a004d4ky3983.mystoryapp.data.remote.response.StoriesResponse
import com.a004d4ky3983.mystoryapp.data.remote.retrofit.ApiConfig
import com.a004d4ky3983.mystoryapp.data.remote.retrofit.ApiService
import com.a004d4ky3983.mystoryapp.util.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val storyDatabase: StoryDatabase,
    private val userPreference: UserPreference,
    private var apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.logout()
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun register(name: String, email: String, password: String): Result<RegisterResponse> {
        return try {
            val response = apiService.register(name, email, password)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.message ?: R.string.an_unknown_error_occurred.toString())
        }
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        wrapEspressoIdlingResource {
            return try {
                val response = apiService.login(email, password)
                Result.Success(response)
            } catch (e: Exception) {
                Result.Error(e.message ?: R.string.an_unknown_error_occurred.toString())
            }
        }
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }

    suspend fun getStoriesLocation(): Result<StoriesResponse> {
        return try {
            val response = apiService.getStoriesWithLocation()
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.message ?: R.string.an_unknown_error_occurred.toString())
        }
    }

    suspend fun uploadStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): Result<FileUploadResponse> {
        return try {
            val response = apiService.uploadImage(file, description, lat, lon)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.message ?: R.string.an_unknown_error_occurred.toString())
        }
    }

    fun updateToken(token: String) {
        instance?.let {
            it.apiService = ApiConfig.getApiService(token)
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            storyDatabase: StoryDatabase,
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository {
            instance?.let { return it }
            return synchronized(this) {
                val newInstance = UserRepository(storyDatabase, userPreference, apiService)
                instance = newInstance
                newInstance
            }
        }
    }
}