package com.dicoding.storyappaaz.utils.`object`

import android.content.Context
import com.dicoding.storyappaaz.api.services.ApiConfig
import com.dicoding.storyappaaz.database.StoryDatabase
import com.dicoding.storyappaaz.utils.PreferenceHelper
import com.dicoding.storyappaaz.database.StoryRepository

object Injection {

    private val apiConfig: ApiConfig = ApiConfig()

    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = apiConfig.getApiService()
        val preferenceHelper = PreferenceHelper(context)
        val token = "Bearer ${preferenceHelper.token}"
        return StoryRepository(database, apiService, token)
    }
}