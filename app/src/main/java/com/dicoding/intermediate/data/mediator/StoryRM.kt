package com.dicoding.intermediate.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dicoding.intermediate.data.db.StoryDatabase
import com.dicoding.intermediate.data.md.Remote
import com.dicoding.intermediate.data.md.Story
import com.dicoding.intermediate.data.network.services.StoryServ

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: StoryServ
) : RemoteMediator<Int, Story>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Story>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH ->{
                val remote = getRemoteKeyClosestToCurrentPosition(state)
                remote?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remote = getRemoteKeyForFirstItem(state)
                val prevKey = remote?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remote != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remote = getRemoteKeyForLastItem(state)
                val nextKey = remote?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remote != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.fetchStories(page, state.config.pageSize)

            val endOfPaginationReached = responseData.data.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteDao().deleteRemote()
                    database.getStoryDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.data.map {
                    Remote(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteDao().insertAll(keys)
                database.getStoryDao().insertAllStories(responseData.data)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Story>): Remote? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteDao().getRemoteId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Story>): Remote? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteDao().getRemoteId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Story>): Remote? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteDao().getRemoteId(id)
            }
        }
    }

}