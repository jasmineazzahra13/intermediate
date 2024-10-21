package com.dicoding.intermediate.data.repo

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.intermediate.data.md.Story
import com.dicoding.intermediate.data.network.ApiResponse
import com.dicoding.intermediate.data.resource.StoryDS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

class StoryRepo (
    private val dataSource: StoryDS,
) {

    suspend fun getStories(isLocationEnabled: Boolean): Flow<ApiResponse<List<Story>>> =
        dataSource.fetchStories(isLocationEnabled).flowOn(Dispatchers.IO)

    suspend fun getDetailStory(id: String): Flow<ApiResponse<Story>> =
        dataSource.fetchDetailStory(id).flowOn(Dispatchers.IO)

    suspend fun addStory(
        photo: File,
        description: String,
        latitude: Float?,
        longitude: Float?
    ): Flow<ApiResponse<String>> =
        dataSource.addStory(photo, description, latitude, longitude).flowOn(Dispatchers.IO)

    fun getPagingStories(): LiveData<PagingData<Story>> {
        return dataSource.fetchPagingStories()

    }
}