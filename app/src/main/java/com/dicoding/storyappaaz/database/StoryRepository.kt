package com.dicoding.storyappaaz.database

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyappaaz.api.services.StoryApi
import com.dicoding.storyappaaz.model.Story
import com.dicoding.storyappaaz.utils.StoryPagingSource

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: StoryApi, private val token: String) {
    fun getStory(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }
}
