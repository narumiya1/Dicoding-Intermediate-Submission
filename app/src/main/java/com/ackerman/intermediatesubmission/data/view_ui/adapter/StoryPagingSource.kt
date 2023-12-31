package com.ackerman.intermediatesubmission.data.view_ui.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ackerman.intermediatesubmission.data.local.UserPreference
import com.ackerman.intermediatesubmission.data.remote.api.ApiService
import com.ackerman.intermediatesubmission.data.remote.response.StoryResponse
import kotlinx.coroutines.flow.first

class StoryPagingSource (private val apiService: ApiService, private val pref: UserPreference) : PagingSource<Int, StoryResponse.StoryApp>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponse.StoryApp> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val token = "Bearer ${pref.getUserData().first().token}"
            val responseData = apiService.getAllStory(token, page, params.loadSize).listStory

            LoadResult.Page(
                data = responseData,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (responseData.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryResponse.StoryApp>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}