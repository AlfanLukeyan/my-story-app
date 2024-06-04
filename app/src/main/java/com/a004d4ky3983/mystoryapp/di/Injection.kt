package com.a004d4ky3983.mystoryapp.di

import android.content.Context
import com.a004d4ky3983.mystoryapp.data.UserRepository
import com.a004d4ky3983.mystoryapp.data.local.StoryDatabase
import com.a004d4ky3983.mystoryapp.data.preference.UserPreference
import com.a004d4ky3983.mystoryapp.data.preference.dataStore
import com.a004d4ky3983.mystoryapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = StoryDatabase.getDatabase(context)
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(database, pref, apiService)
    }
}