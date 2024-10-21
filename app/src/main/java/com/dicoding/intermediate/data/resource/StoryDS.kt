package com.dicoding.intermediate.data.resource

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.intermediate.data.db.StoryDatabase
import com.dicoding.intermediate.data.md.Story
import com.dicoding.intermediate.data.mediator.StoryRemoteMediator
import com.dicoding.intermediate.data.network.ApiResponse
import com.dicoding.intermediate.data.network.services.StoryServ
import com.dicoding.intermediate.utils.constant.ConstantApp
import com.dicoding.intermediate.utils.helper.createResponse
import com.dicoding.intermediate.utils.helper.toMultipart
import com.dicoding.intermediate.utils.helper.toRequestBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File

class StoryDS(private val database: StoryDatabase, private val userService: StoryServ) {

    fun fetchPagingStories(): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(database, userService),
            pagingSourceFactory = {
                database.getStoryDao().getAllStories()
            }
        ).liveData
    }

    suspend fun fetchStories(isLocationEnabled: Boolean = false): Flow<ApiResponse<List<Story>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = userService.fetchStories(
                    location = if(isLocationEnabled) 1 else 0
                )
                if (response.error) {
                    emit(ApiResponse.Error(response.message))
                    return@flow
                }

                if (response.data.isEmpty()) {
                    emit(ApiResponse.Empty)
                    return@flow
                }

                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.createResponse()?.message ?: ""))
            }
        }
    }

    suspend fun fetchDetailStory(id: String): Flow<ApiResponse<Story>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = userService.fetchDetailStory(id)
                if (response.error) {
                    emit(ApiResponse.Error(response.message))
                    return@flow
                }
                emit(ApiResponse.Success(response.data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.createResponse()?.message ?: ""))
            }
        }
    }

    suspend fun addStory(
        photo: File,
        description: String,
        latitude: Float?,
        longitude: Float?
    ): Flow<ApiResponse<String>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = userService.addStory(
                    photo = photo.toMultipart(),
                    description = description.toRequestBody(),
                    latitude = latitude.toRequestBody(),
                    longitude = longitude.toRequestBody(),
                )

                if (response.error) {
                    emit(ApiResponse.Error(response.message))
                    return@flow
                }

                emit(ApiResponse.Success(response.message))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.createResponse()?.message ?: ""))
            }
        }
    }
}