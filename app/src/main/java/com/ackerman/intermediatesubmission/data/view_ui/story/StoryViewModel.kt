package com.ackerman.intermediatesubmission.data.view_ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ackerman.intermediatesubmission.data.local.UserModel
import com.ackerman.intermediatesubmission.data.remote.response.StoryResponse
import com.ackerman.intermediatesubmission.data.repository.DataStoryRepository

class StoryViewModel (private val repository: DataStoryRepository) : ViewModel() {
    fun getStory() :LiveData<PagingData<StoryResponse.StoryApp>>{
        return repository.getAllzStory().cachedIn(viewModelScope)
    }

    fun getCurrentUser(): LiveData<UserModel>{
        return repository.getCurrentUserData()
    }
}